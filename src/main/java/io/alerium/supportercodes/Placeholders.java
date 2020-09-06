package io.alerium.supportercodes;

import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

final class Placeholders extends PlaceholderExpansion {

    private static final int CURRENT_MONTH = new Date().getMonth();
    private final SupporterCodesPlugin plugin;

    Placeholders(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "supportercodes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Frcsty";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1-Alpha";
    }

    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        final InformationStorage storage = plugin.getInformationStorage();
        final Supporter supporter = storage.getSupporter(player.getUniqueId());

        if (supporter == null) {
            final Creator creator = storage.getCreator(player.getUniqueId());

            switch (params) {
                case "creator_supporters_monthly":
                    return String.valueOf(getMonthlySupporters(storage));
                case "creator_supporters_all":
                    return String.valueOf(creator.getSupporters());
            }
            return null;
        }

        final Player supportedCreator = Bukkit.getPlayer(supporter.getSupporting());
        switch (params) {
            case "currently_supporting":
                return supportedCreator == null ? "None" : supportedCreator.getName();
            case "supporting_since":
                return supportedCreator == null ? "None" : new Date(supporter.getSupportingSince()).toString();
        }

        return null;
    }

    private long getMonthlySupporters(final InformationStorage storage) {
        long result = 0;
        for (final Supporter supporter : storage.getSupporters().values()) {
            if (new Date(supporter.getSupportingSince()).getMonth() == CURRENT_MONTH) {
                result += 1;
            }
        }

        return result;
    }
}

package io.alerium.supportercodes;

import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.InformationWrapper;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

final class Placeholder extends PlaceholderExpansion {

    private static final int CURRENT_MONTH = new Date().getMonth();
    private final InformationHandler handler;

    Placeholder(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
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
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        final UUID playerUUID = player.getUniqueId();

        if (params.contains("currently_supporting")) {
            if (handler.isCreator(playerUUID)) {
                return "N/A";
            }

            final SupporterWrapper wrapper = (SupporterWrapper) handler.getWrapper(playerUUID);
            final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(UUID.fromString(wrapper.getSupportedCreatorID()));

            if (creatorPlayer == null) {
                return "None";
            }

            return creatorPlayer.getName();
        } else if (params.contains("supporting_since")) {
            if (handler.isCreator(playerUUID)) {
                return "N/A";
            }

            final SupporterWrapper wrapper = (SupporterWrapper) handler.getWrapper(playerUUID);

            if (wrapper.getSupporterSince() == 0) {
                return "Not Supporting";
            }

            return new Date(wrapper.getSupporterSince()).toString();
        } else if (params.contains("creator_supporters_all")) {
            final String[] data = params.split("_");
            final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(data[3]);

            if (!handler.isCreator(creatorPlayer.getUniqueId())) {
                return "Invalid Creator";
            }

            final CreatorWrapper wrapper = (CreatorWrapper) handler.getWrapper(creatorPlayer.getUniqueId());

            return String.valueOf(wrapper.getSupporters());
        } else if (params.contains("creator_supporters_monthly")) {
            final String[] data = params.split("_");
            final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(data[3]);

            if (!handler.isCreator(creatorPlayer.getUniqueId())) {
                return "Invalid Creator";
            }

            final CreatorWrapper wrapper = (CreatorWrapper) handler.getWrapper(creatorPlayer.getUniqueId());

            return String.valueOf(getMonthlySupporter(wrapper));
        }
        return null;
    }

    private long getMonthlySupporter(final CreatorWrapper creator) {
        long supporters = 0;
        for (final InformationWrapper wrapper : handler.getWrappers().values()) {
            if (wrapper instanceof CreatorWrapper) {
                continue;
            }

            final SupporterWrapper supporterWrapper = (SupporterWrapper) wrapper;
            if (!supporterWrapper.getSupportedCreatorID().equalsIgnoreCase(creator.getUserID().toString())) {
                continue;
            }

            final Date date = new Date(supporterWrapper.getSupporterSince());
            if (date.getMonth() == CURRENT_MONTH) {
                supporters += 1;
            }
        }

        return supporters;
    }
}

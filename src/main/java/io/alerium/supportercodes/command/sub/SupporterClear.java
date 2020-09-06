package io.alerium.supportercodes.command.sub;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Replace;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Command("support")
public final class SupporterClear extends CommandBase {

    private static final String SUB_COMMAND = "stop";
    private static final String ALT_SUB_COMMAND = "clear";
    private static final String PERMISSION = "supportercodes.command.clear";
    private final SupporterCodesPlugin plugin;
    private final ConfigurationSection messages;

    public SupporterClear(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;

        this.messages = plugin.getConfig().getConfigurationSection("messages");
    }

    @SubCommand(SUB_COMMAND)
    @Permission(PERMISSION)
    public void onCommand(final Player player) {
        final InformationStorage storage = plugin.getInformationStorage();
        final Supporter supporter = storage.getSupporter(player.getUniqueId());

        if (supporter == null) {
            Color.colorize(
                    messages.getStringList("not-supporting-a-creator"),
                    player
            ).forEach(player::sendMessage);
            return;
        }

        final Player supporting = Bukkit.getPlayer(supporter.getSupporting());
        supporter.setSupporting(null);
        supporter.setSupporterSince(null);

        storage.setSupporter(supporter.getId(), supporter);
        Color.colorize(Replace.replaceList(
                messages.getStringList("no-longer-supporting-a-creator"),
                "{creator-name}", supporting.getName()
        ), player).forEach(player::sendMessage);
    }

    @SubCommand(ALT_SUB_COMMAND)
    @Permission(PERMISSION)
    public void onAltComand(final Player player) {
        onCommand(player);
    }
}

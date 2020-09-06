package io.alerium.supportercodes.command;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.listener.event.PlayerSupportCreatorEvent;
import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import io.alerium.supportercodes.util.Color;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@Command("support")
public final class PluginCommand extends CommandBase {

    private final ConfigurationSection messages;
    private final SupporterCodesPlugin plugin;

    public PluginCommand(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getConfigurationSection("messages");
    }

    @Default
    public void onCommand(final CommandSender sender, final String[] args) {
        final InformationStorage storage = plugin.getInformationStorage();

        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Supporter supporter = storage.getSupporter(player.getUniqueId());

            if (args.length == 0) {
                if (supporter.getSupporting() == null) {
                    Color.colorize(
                            messages.getStringList("how-to-support"),
                            player
                    ).forEach(player::sendMessage);
                    return;
                }

                Color.colorize(
                        messages.getStringList("current-supported-creator"),
                        player
                ).forEach(player::sendMessage);
                return;
            }

            if (args.length == 2) {
                final String creatorIdentifier = args[1];
                final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(creatorIdentifier);
                final Creator creator = storage.getCreator(creatorPlayer.getUniqueId());

                if (creator == null) {
                    Color.colorize(
                            messages.getStringList("invalid-creator"),
                            player
                    ).forEach(player::sendMessage);
                    return;
                }

                supporter.setSupporting(creator.getId().toString());
                creator.incrementSupporters();

                storage.setSupporter(player.getUniqueId(), supporter);
                storage.setCreator(creator.getPlayer().getUniqueId(), creator);

                Color.colorize(
                        messages.getStringList("started-supporting-a-creator"),
                        player
                ).forEach(player::sendMessage);

                Bukkit.getServer().getPluginManager().callEvent(new PlayerSupportCreatorEvent());
                return;
            }
        }

        if (args.length == 3) {
            final String creatorIdentifier = args[1];
            final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(creatorIdentifier);
            final Creator creator = storage.getCreator(creatorPlayer.getUniqueId());

            if (creator == null) {
                Color.colorize(
                        messages.getStringList("invalid-creator"),
                        sender
                ).forEach(sender::sendMessage);
                return;
            }

            final String forcePlayerIdentifier = args[2];
            final Player forcePlayer = Bukkit.getPlayerExact(forcePlayerIdentifier);

            if (forcePlayer == null) {
                Color.colorize(
                        messages.getStringList("invalid-force-player"),
                        sender
                ).forEach(sender::sendMessage);
                return;
            }

            final Supporter forceSupporter = storage.getSupporter(forcePlayer.getUniqueId());
            if (forceSupporter == null) {
                Color.colorize(
                    messages.getStringList("invalid-force-supporter"),
                        sender
                ).forEach(sender::sendMessage);
                return;
            }

            forceSupporter.setSupporting(creator.getId().toString());
            creator.incrementSupporters();

            storage.setSupporter(forceSupporter.getId(), forceSupporter);
            storage.setCreator(creator.getId(), creator);

            Bukkit.getServer().getPluginManager().callEvent(new PlayerSupportCreatorEvent());
        }
    }

}

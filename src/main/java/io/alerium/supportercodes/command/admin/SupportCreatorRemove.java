package io.alerium.supportercodes.command.admin;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import io.alerium.supportercodes.util.Replace;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

final class SupportCreatorRemove {

    private final InformationHandler handler;
    private final MessageStorage message;

    SupportCreatorRemove(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();
    }

    void onCommand(final CommandSender sender, final String target) {
        final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        if (targetPlayer == null) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.INVALID_CREATOR),
                    sender
            ));
            return;
        }

        if (!handler.wrapperExists(targetPlayer.getUniqueId())) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.INVALID_CREATOR),
                    sender
            ));
            return;
        }

        handler.removeWrapper(targetPlayer.getUniqueId());

        Message.send(sender, Color.colorize(
                Replace.replaceList(
                        message.getMessage(Identifier.REMOVED_CREATOR),
                        "{target}", target
                ),
                sender
        ));
    }
}

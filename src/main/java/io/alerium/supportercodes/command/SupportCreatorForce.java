package io.alerium.supportercodes.command;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

final class SupportCreatorForce {

    private final InformationHandler handler;
    private final MessageStorage message;

    SupportCreatorForce(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();
    }

    void onCommand(final Player sender, final String targetCreator, final String target) {
        final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        if (targetPlayer == null) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.INVALID_FORCE_PLAYER),
                    sender
            ));
            return;
        }

        if (handler.isCreator(targetPlayer.getUniqueId())) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.CREATOR_FORCE_CREATOR),
                    sender
            ));
            return;
        }

        final OfflinePlayer targetCreatorPlayer = Bukkit.getOfflinePlayer(targetCreator);

        if (targetCreatorPlayer == null) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.INVALID_CREATOR),
                    sender
            ));
            return;
        }

        if (!handler.isCreator(targetCreatorPlayer.getUniqueId())) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.INVALID_CREATOR),
                    sender
            ));
            return;
        }

        handler.setUserSupporting(targetPlayer.getUniqueId(), targetCreatorPlayer.getUniqueId());
    }
}

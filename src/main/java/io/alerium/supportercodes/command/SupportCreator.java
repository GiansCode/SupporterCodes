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

import java.util.UUID;

final class SupportCreator {

    private final InformationHandler handler;
    private final MessageStorage message;

    SupportCreator(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();
    }

    void onCommand(final Player player, final String target) {
        final UUID uuid = player.getUniqueId();

        if (handler.isSupporting(uuid)) {
            Message.send(player, Color.colorize(
                    message.getMessage(Identifier.USER_IS_SUPPORTING_CREATOR),
                    player
            ));
            return;
        }

        final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(target);
        if (creatorPlayer == null || !handler.wrapperExists(creatorPlayer.getUniqueId()) || !handler.isCreator(creatorPlayer.getUniqueId())) {
            Message.send(player, Color.colorize(
                    message.getMessage(Identifier.INVALID_CREATOR),
                    player
            ));
            return;
        }

        handler.setUserSupporting(uuid, creatorPlayer.getUniqueId());
        Message.send(player, Color.colorize(
                message.getMessage(Identifier.USER_IS_SUPPORTING_CREATOR),
                player
        ));
    }

}

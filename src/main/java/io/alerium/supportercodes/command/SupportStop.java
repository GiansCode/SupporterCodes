package io.alerium.supportercodes.command;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.InformationWrapper;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(Identifier.BASE_COMMAND)
public final class SupportStop extends CommandBase {

    private final InformationHandler handler;
    private final MessageStorage message;

    public SupportStop(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();
    }

    @SubCommand(Identifier.STOP_SUB_COMMAND)
    @Permission(Identifier.STOP_PERMISSION)
    public void onCommand(final Player player) {
        handle(player);
    }

    @SubCommand(Identifier.CLEAR_SUB_COMMAND)
    @Permission(Identifier.STOP_PERMISSION)
    public void onAltCommand(final Player player) {
        handle(player);
    }

    private void handle(final Player player) {
        final UUID uuid = player.getUniqueId();
        final InformationWrapper wrapper = handler.getWrapper(uuid);

        if (wrapper instanceof CreatorWrapper) {
            return;
        }

        if (handler.isSupporting(uuid)) {
            Message.send(player, Color.colorize(
                    message.getMessage(Identifier.USER_STOPPED_SUPPORTING_CREATOR),
                    player
            ));

            handler.stopUserSupporting(uuid);
            return;
        }

        Message.send(player, Color.colorize(
                message.getMessage(Identifier.USER_IS_NOT_SUPPORT_A_CREATOR),
                player
        ));
    }

}

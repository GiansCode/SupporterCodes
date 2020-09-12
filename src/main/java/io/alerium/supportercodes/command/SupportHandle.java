package io.alerium.supportercodes.command;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import io.alerium.supportercodes.listener.event.CreatorSupportEvent;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@Command(Identifier.BASE_COMMAND)
public final class SupportHandle extends CommandBase {

    private final InformationHandler handler;
    private final MessageStorage message;

    public SupportHandle(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();
    }

    @SubCommand(Identifier.HANDLE_SUB_COMMAND)
    @Permission(Identifier.HANDLE_PERMISSION)
    public void onCommand(final CommandSender sender, final String target) {
        final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        final UUID targetUUID = targetPlayer.getUniqueId();

        if (handler.isCreator(targetUUID)) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.USER_IS_A_CREATOR),
                    sender
            ));
            return;
        }

        if (!handler.wrapperExists(targetUUID)) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.ALREADY_EXISTENT_WRAPPER),
                    sender
            ));
            return;
        }

        if (!handler.isSupporting(targetUUID)) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.USER_IS_NOT_SUPPORT_A_CREATOR),
                    sender
            ));
            return;
        }

        handler.handleSupportCodeUses(targetUUID);
        Bukkit.getServer().getPluginManager().callEvent(new CreatorSupportEvent(
                (SupporterWrapper) handler.getWrapper(targetUUID)
        ));
    }

}

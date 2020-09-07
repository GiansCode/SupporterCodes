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
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(Identifier.BASE_COMMAND)
public final class SupportCommand extends CommandBase {

    private final InformationHandler handler;
    private final MessageStorage message;

    private final SupportCreator supportCreator;
    private final SupportCreatorForce supportCreatorForce;

    public SupportCommand(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
        this.message = plugin.getMessageStorage();

        this.supportCreator = new SupportCreator(plugin);
        this.supportCreatorForce = new SupportCreatorForce(plugin);
    }

    @Default
    @Permission(Identifier.BASE_PERMISSION)
    public void onCommand(final Player player, final String[] arguments) {
        if (arguments.length == 1) {
            if (!player.hasPermission(Identifier.SUPPORT_PERMISSION)) {
                Message.send(player, Color.colorize(
                        message.getMessage(Identifier.MISSING_PERMISSION),
                        player
                ));
                return;
            }

            this.supportCreator.onCommand(player, arguments[0]);
            return;
        }

        if (arguments.length == 2) {
            if (!player.hasPermission(Identifier.SUPPORT_FORCE_PERMISSION)) {
                Message.send(player, Color.colorize(
                        message.getMessage(Identifier.MISSING_PERMISSION),
                        player
                ));
                return;
            }

            this.supportCreatorForce.onCommand(player, arguments[0], arguments[1]);
            return;
        }

        final UUID uuid = player.getUniqueId();
        final InformationWrapper wrapper = handler.getWrapper(uuid);

        if (wrapper instanceof CreatorWrapper) {
            Message.send(player, Color.colorize(
                    message.getMessage(Identifier.USER_IS_A_CREATOR),
                    player
            ));
            return;
        }

        if (handler.isSupporting(uuid)) {
            Message.send(player, Color.colorize(
                    message.getMessage(Identifier.USER_IS_SUPPORTING_CREATOR),
                    player
            ));
            return;
        }

        Message.send(player, Color.colorize(
                message.getMessage(Identifier.USER_IS_NOT_SUPPORT_A_CREATOR),
                player
        ));
    }

}

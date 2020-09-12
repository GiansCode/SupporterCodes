package io.alerium.supportercodes.command.admin;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

@Command(Identifier.BASE_COMMAND)
public final class SupportCreatorManage extends CommandBase {

    private final MessageStorage message;

    private final SupportCreatorAdd addCreator;
    private final SupportCreatorRemove removeCreator;

    public SupportCreatorManage(final SupporterCodesPlugin plugin) {
        this.message = plugin.getMessageStorage();

        this.addCreator = new SupportCreatorAdd(plugin);
        this.removeCreator = new SupportCreatorRemove(plugin);
    }

    @SubCommand(Identifier.CREATOR_MANAGE_COMMAND)
    public void onCommand(final CommandSender sender, final String[] arguments) {
        if (arguments.length != 3) {
            Message.send(sender, Color.colorize(
                    message.getMessage(Identifier.WRONG_COMMAND_USAGE),
                    sender
            ));
            return;
        }

        final String argument = arguments[1];
        switch (argument.toLowerCase()) {
            case Identifier.CREATOR_ADD_COMMAND:
                if (!sender.hasPermission(Identifier.CREATOR_ADD_PERMISSION)) {
                    break;
                }

                this.addCreator.onCommand(sender, arguments[2]);
                return;
            case Identifier.CREATOR_REMOVE_COMMAND:
                if (!sender.hasPermission(Identifier.CREATOR_REMOVE_PERMISSION)) {
                    break;
                }

                this.removeCreator.onCommand(sender, arguments[2]);
                return;
        }

        Message.send(sender, Color.colorize(
                message.getMessage(Identifier.MISSING_PERMISSION),
                sender
        ));
    }

}

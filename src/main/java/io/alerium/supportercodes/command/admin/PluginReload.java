package io.alerium.supportercodes.command.admin;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

@Command(Identifier.BASE_COMMAND)
public final class PluginReload extends CommandBase {

    private final SupporterCodesPlugin plugin;

    public PluginReload(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @SubCommand(Identifier.RELOAD_SUB_COMMAND)
    @Permission(Identifier.RELOAD_PERMISSION)
    public void onCommand(final CommandSender sender) {
        final MessageStorage message = plugin.getMessageStorage();

        plugin.reloadConfig();
        message.load(plugin);

        Message.send(sender, Color.colorize(
                message.getMessage(Identifier.RELOAD_PLUGIN),
                sender
        ));
    }

}

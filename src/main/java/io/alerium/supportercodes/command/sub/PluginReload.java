package io.alerium.supportercodes.command.sub;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Replace;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

@Command("support")
public final class PluginReload extends CommandBase {

    private static final String SUB_COMMAND = "reload";
    private static final String PERMISSION = "supportercodes.command.reload";
    private final SupporterCodesPlugin plugin;
    private final ConfigurationSection messages;

    public PluginReload(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getConfigurationSection("messages");
    }

    @SubCommand(SUB_COMMAND)
    @Permission(PERMISSION)
    public void onCommand(final CommandSender sender) {
        final long start = System.currentTimeMillis();
        plugin.reloadConfig();

        plugin.getCommandHandler().setMessages();
        plugin.updateMenuFactory();
        final String estimatedTime = String.valueOf(System.currentTimeMillis() - start);
        Color.colorize(Replace.replaceList(
                messages.getStringList("reloaded-plugin"),
                "{time}", estimatedTime),
                sender
        ).forEach(sender::sendMessage);
    }

}

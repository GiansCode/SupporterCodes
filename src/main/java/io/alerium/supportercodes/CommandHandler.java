package io.alerium.supportercodes;

import io.alerium.supportercodes.command.PluginCommand;
import io.alerium.supportercodes.command.sub.*;
import io.alerium.supportercodes.util.Color;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.MessageHandler;
import org.bukkit.configuration.ConfigurationSection;

public final class CommandHandler {

    private final SupporterCodesPlugin plugin;
    private CommandManager manager;

    CommandHandler(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    void setup() {
        this.manager = new CommandManager(plugin);

        manager.register(
                new PluginReload(plugin),
                new SupporterClear(plugin),
                new SupporterHandle(plugin),
                new SupporterList(plugin),
                new PluginCommand(plugin),

                new DBInfo(plugin)
        );

        setMessages();
    }

    public void setMessages() {
        final ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
        final MessageHandler handler = manager.getMessageHandler();

        handler.register("cmd.no.console", (sender ->
                Color.colorize(messages.getStringList("non-console-command"), sender).forEach(sender::sendMessage)));

        handler.register("cmd.no.permission", (sender ->
                Color.colorize(messages.getStringList("no-command-permission"), sender).forEach(sender::sendMessage)));

        handler.register("cmd.no.exists", (sender ->
                Color.colorize(messages.getStringList("non-existent-command"), sender).forEach(sender::sendMessage)));

        handler.register("cmd.wrong.usage", (sender ->
                Color.colorize(messages.getStringList("wrong-command-usage"), sender).forEach(sender::sendMessage)));
    }
}

package io.alerium.supportercodes.message;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.base.MessageHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;

public final class MessageStorage {

    private final Map<String, List<String>> messages = new HashMap<>();

    public void load(final SupporterCodesPlugin plugin) {
        messages.clear();

        final FileConfiguration config = plugin.getConfig();
        final ConfigurationSection messageSection = config.getConfigurationSection("messages");

        if (messageSection == null) {
            plugin.getLogger().log(Level.WARNING, "Configuration section 'messages' is null!");
            return;
        }

        for (final String key : messageSection.getKeys(false)) {
            messages.put(key, messageSection.getStringList(key));
        }

        final MessageHandler handler = plugin.getCommandManager().getMessageHandler();

        handler.register("cmd.no.console", (sender ->
                Message.send(sender, Color.colorize(getMessage(Identifier.NON_CONSOLE_COMMAND), sender))));

        handler.register("cmd.no.permission", (sender ->
                Message.send(sender, Color.colorize(getMessage(Identifier.MISSING_PERMISSION), sender))));

        handler.register("cmd.no.exists", (sender ->
                Message.send(sender, Color.colorize(getMessage(Identifier.NON_EXISTENT_COMMAND), sender))));

        handler.register("cmd.wrong.usage", (sender ->
                Message.send(sender, Color.colorize(getMessage(Identifier.WRONG_COMMAND_USAGE), sender))));
    }

    public List<String> getMessage(final String key) {
        return messages.get(key) == null
                ? Collections.singletonList(String.format("Key '%s' does not have any message attached!", key))
                : messages.get(key);
    }
}

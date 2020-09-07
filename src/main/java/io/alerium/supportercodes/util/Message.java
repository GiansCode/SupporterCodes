package io.alerium.supportercodes.util;

import org.bukkit.command.CommandSender;

import java.util.List;

public final class Message {

    public static void send(final CommandSender sender, final List<String> message) {
        message.forEach(sender::sendMessage);
    }

}

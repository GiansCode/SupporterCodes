package io.alerium.supportercodes.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public final class Color {

    private static String colorize(final String message, final Player player) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, message));
    }

    public static List<String> colorize(final List<String> message, final CommandSender sender) {
        return message.stream().map(msg -> {
            if (sender instanceof Player) {
                return colorize(msg, (Player) sender);
            } else {
                return colorize(msg, null);
            }
        }).collect(Collectors.toList());
    }

}

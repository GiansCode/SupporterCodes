package io.alerium.supportercodes.command;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.command.factory.MenuFactory;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;

import java.util.logging.Level;

@Command(Identifier.BASE_COMMAND)
public final class SupportList extends CommandBase {

    private final SupporterCodesPlugin plugin;

    public SupportList(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @SubCommand(Identifier.CREATOR_LIST_COMMAND)
    @Permission(Identifier.CREATOR_LIST_PERMISSION)
    public void onCommand(final Player player) {
        if (plugin.getInformationHandler().isCreator(player.getUniqueId())) {
            Message.send(player, Color.colorize(
                    plugin.getMessageStorage().getMessage(Identifier.USER_IS_A_CREATOR),
                    player
            ));
            return;
        }

        final MenuFactory factory = new MenuFactory(plugin, player);
        final PaginatedGui menu = factory.generate();

        if (menu == null) {
            plugin.getLogger().log(Level.WARNING, "Creator menu was null for user " + player.getName());
            return;
        }

        menu.open(player);
    }
}

package io.alerium.supportercodes.command.sub;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.factory.MenuFactory;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;

import java.util.logging.Level;

@Command("support")
public final class SupporterList extends CommandBase {

    private static final String SUB_COMMAND = "list";
    private static final String PERMISSION = "supportercodes.command.stop";
    private final SupporterCodesPlugin plugin;

    public SupporterList(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @SubCommand(SUB_COMMAND)
    @Permission(PERMISSION)
    public void onCommand(final Player player) {
        final MenuFactory factory = plugin.getMenuFactory();

        factory.setCurrentPlayer(player);
        final PaginatedGui menu = factory.getMenu();

        if (menu == null) {
            plugin.getLogger().log(Level.WARNING, "The creator menu is null for player " + player.getName() + "(" + player.getUniqueId() +")");
            return;
        }

        menu.open(player);
    }
}

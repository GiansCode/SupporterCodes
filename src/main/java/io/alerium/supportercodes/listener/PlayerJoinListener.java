package io.alerium.supportercodes.listener;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public final class PlayerJoinListener implements Listener {

    private final InformationHandler handler;

    public PlayerJoinListener(final SupporterCodesPlugin plugin) {
        this.handler = plugin.getInformationHandler();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (!handler.wrapperExists(uuid)) {
            handler.setWrapper(uuid, new SupporterWrapper(player));
        }
    }
}

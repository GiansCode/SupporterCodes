package io.alerium.supportercodes.listener;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final SupporterCodesPlugin plugin;

    public PlayerJoinListener(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final InformationStorage storage = plugin.getInformationStorage();
        final Player player = event.getPlayer();

        if (storage.getSupporter(player.getUniqueId()) == null) {
            final Supporter supporter = new Supporter(player.getUniqueId());

            storage.setSupporter(player.getUniqueId(), supporter);
            plugin.getInformationHandler().setSupporter(supporter);
        }
    }

}

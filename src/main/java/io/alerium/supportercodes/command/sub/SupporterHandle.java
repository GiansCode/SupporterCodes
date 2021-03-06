package io.alerium.supportercodes.command.sub;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.listener.event.CreatorSupportEvent;
import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command("support")
public final class SupporterHandle extends CommandBase {

    private static final String SUB_COMMAND = "handle";
    private static final String PERMISSION = "supportercodes.command.handle";
    private final SupporterCodesPlugin plugin;

    public SupporterHandle(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @SubCommand(SUB_COMMAND)
    @Permission(PERMISSION)
    public void onCommand(final CommandSender sender, final String target) {
        final InformationStorage storage = plugin.getInformationStorage();
        final Player player = Bukkit.getPlayer(target);
        final Supporter supporter = storage.getSupporter(player.getUniqueId());

        if (supporter.getSupporting() == null) {
            return;
        }
        final Creator creator = storage.getCreator(UUID.fromString(supporter.getSupporting()));

        creator.incrementCodeUses();
        storage.setCreator(creator.getId(), creator);

        Bukkit.getServer().getPluginManager().callEvent(new CreatorSupportEvent());
    }

}

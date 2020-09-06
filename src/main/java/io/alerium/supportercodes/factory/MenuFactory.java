package io.alerium.supportercodes.factory;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.listener.event.PlayerSupportCreatorEvent;
import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Replace;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class MenuFactory {

    private PaginatedGui menu;

    public void updateMenu(final SupporterCodesPlugin plugin, final Player player) {
        this.menu = createMenu(plugin, player);
    }

    private PaginatedGui createMenu(final SupporterCodesPlugin plugin, final Player player) {
        final FileConfiguration config = plugin.getConfig();
        final ConfigurationSection messages = config.getConfigurationSection("messages");
        final InformationStorage storage = plugin.getInformationStorage();
        final ConfigurationSection menuSection = config.getConfigurationSection("creator-menu");
        if (menuSection == null) {
            plugin.getLogger().log(Level.WARNING, "Configuration section 'creator-menu' could not be found!");
            return null;
        }

        final PaginatedGui gui = new PaginatedGui(
                menuSection.getInt("page-rows"),
                menuSection.getInt("page-size"),
                Color.colorize(menuSection.getString("page-title"), player)
        );

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        final ConfigurationSection creatorMenuSection = menuSection.getConfigurationSection("creator-display");
        if (creatorMenuSection == null) {
            plugin.getLogger().log(Level.WARNING, "Configuration section 'creator-display' could not be found!");
            return null;
        }

        final String materialString = creatorMenuSection.getString("material");
        final Material material = Material.getMaterial(materialString == null ? "STONE" : materialString);
        final ItemStack baseItem = new ItemStack(material, creatorMenuSection.getInt("amount"), (short) creatorMenuSection.getInt("data"));
        final Supporter supporter = storage.getSupporter(player.getUniqueId());

        final String displayName = creatorMenuSection.getString("display");
        final List<String> displayLore = creatorMenuSection.getStringList("lore");

        for (final UUID creatorUUID : storage.getCreators().keySet()) {
            final Creator creator = storage.getCreators().get(creatorUUID);
            final ItemStack item = baseItem.clone();

            final SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            if (skullMeta != null) {
                skullMeta.setOwner(creator.getPlayer().getName());
            }

            item.setItemMeta(skullMeta);

            final ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                final String creatorName = creator.getPlayer().getName();
                System.out.println(creatorName);

                meta.setDisplayName(Color.colorize(Replace.replaceString(
                        displayName,
                        "{creator-name}", creatorName
                ), player));

                meta.setLore(Color.colorize(Replace.replaceList(
                        displayLore,
                        "{creator-name}", creatorName
                ), player));

                if (supporter != null && supporter.getSupporting() != null && supporter.getSupporting().equals(creatorUUID)) {
                    meta.addEnchant(Enchantment.DURABILITY, 0, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }

            item.setItemMeta(meta);

            gui.addItem(new GuiItem(item, event -> {
                if (supporter == null) {
                    plugin.getLogger().log(Level.WARNING, "User " + player.getName() + " does not have a Supporter Profile!");
                    return;
                }

                if (supporter.getSupporting() != null) {
                    gui.close(player);
                    Color.colorize(
                            messages.getStringList("max-creators-supported"),
                            player
                    ).forEach(player::sendMessage);
                    return;
                }

                gui.close(player);
                supporter.setSupporting(creator.getId().toString());
                storage.setSupporter(player.getUniqueId(), supporter);
                Color.colorize(
                        messages.getStringList("started-supporting-a-creator"),
                        player
                ).forEach(player::sendMessage);

                Bukkit.getServer().getPluginManager().callEvent(new PlayerSupportCreatorEvent());
            }));
        }

        final ConfigurationSection itemSection = menuSection.getConfigurationSection("items");
        for (final String fillerItem : itemSection.getKeys(false)) {
            final ConfigurationSection fillerItemSection = itemSection.getConfigurationSection(fillerItem);

            final ItemStack item = new ItemStack(
                    Material.getMaterial(fillerItemSection.getString("material")),
                    fillerItemSection.getInt("amount"),
                    (short) fillerItemSection.getInt("data")
            );

            final ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(Color.colorize(
                        fillerItemSection.getString("display"),
                        player
                ));

                meta.setLore(Color.colorize(
                        fillerItemSection.getStringList("lore"),
                        player
                ));

                item.setItemMeta(meta);
            }

            final GuiItem guiItem = new GuiItem(item);
            for (final int slot : fillerItemSection.getIntegerList("slots")) {
                gui.setItem(slot, guiItem);
            }
        }

        return gui;
    }

    public PaginatedGui getMenu() {
        return this.menu;
    }
}

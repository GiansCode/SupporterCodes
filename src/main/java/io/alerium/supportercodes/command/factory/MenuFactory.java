package io.alerium.supportercodes.command.factory;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import io.alerium.supportercodes.listener.event.PlayerSupportCreatorEvent;
import io.alerium.supportercodes.message.MessageStorage;
import io.alerium.supportercodes.util.Color;
import io.alerium.supportercodes.util.Message;
import io.alerium.supportercodes.util.Replace;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;

public final class MenuFactory {

    private final SupporterCodesPlugin plugin;
    private final Player player;
    private final FileConfiguration config;

    public MenuFactory(final SupporterCodesPlugin plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;

        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/creator_menu.yml"));
    }

    public PaginatedGui generate() {
        final PaginatedGui gui = new PaginatedGui(
                config.getInt(Identifier.PAGE_ROWS),
                config.getInt(Identifier.PAGE_SIZE),
                Color.colorize(config.getString(Identifier.PAGE_TITLE), player)
        );

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        generateItems(gui);
        generateCreators(gui);

        return gui;
    }

    private void generateCreators(final PaginatedGui gui) {
        final InformationHandler handler = plugin.getInformationHandler();
        final ConfigurationSection creatorSection = config.getConfigurationSection("creator-display");
        final MessageStorage message = plugin.getMessageStorage();

        final ItemStack baseItemStack = new ItemStack(
                Material.getMaterial(creatorSection.getString(Identifier.MATERIAL)),
                creatorSection.getInt(Identifier.AMOUNT),
                (short) creatorSection.getInt(Identifier.DATA)
        );

        for (final CreatorWrapper wrapper : plugin.getInformationHandler().getCreatorWrappers()) {
            final OfflinePlayer creatorPlayer = Bukkit.getOfflinePlayer(wrapper.getUserID());
            final ItemStack itemStack = baseItemStack.clone();
            final ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(Color.colorize(Replace.replaceString(
                        creatorSection.getString(Identifier.DISPLAY_NAME),
                        "{creator-name}", creatorPlayer.getName()
                ), player));

                meta.setLore(Color.colorize(Replace.replaceList(
                        creatorSection.getStringList(Identifier.LORE),
                        "{creator-name}", creatorPlayer.getName()
                ), player));

                if (handler.isSupportingCreator(player.getUniqueId(), wrapper.getUserID())) {
                    meta.addEnchant(Enchantment.DURABILITY, 0, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }

            itemStack.setItemMeta(meta);
            final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            if (skullMeta != null) {
                skullMeta.setOwner(creatorPlayer.getName());
            }

            itemStack.setItemMeta(skullMeta);

            final SupporterWrapper supporterWrapper = (SupporterWrapper) handler.getWrapper(player.getUniqueId());
            final GuiItem guiItem = new GuiItem(itemStack, event -> {
                if (!supporterWrapper.getSupportedCreatorID().equalsIgnoreCase("none")) {
                    Message.send(
                            player,
                            message.getMessage(Identifier.USER_IS_SUPPORTING_CREATOR)
                    );

                    gui.close(player);
                    return;
                }

                handler.setUserSupporting(player.getUniqueId(), creatorPlayer.getUniqueId());
                Message.send(
                        player,
                        message.getMessage(Identifier.STARTED_SUPPORTING_CREATOR)
                );

                gui.close(player);
                Bukkit.getServer().getPluginManager().callEvent(new PlayerSupportCreatorEvent(supporterWrapper));
            });

            gui.addItem(guiItem);
        }
    }

    private void generateItems(final PaginatedGui gui) {
        final ConfigurationSection items = config.getConfigurationSection("items");

        for (final String item : items.getKeys(false)) {
            final ConfigurationSection itemSection = items.getConfigurationSection(item);
            if (itemSection == null) continue;

            final ItemStack itemStack = new ItemStack(
                    Material.getMaterial(itemSection.getString(Identifier.MATERIAL)),
                    itemSection.getInt(Identifier.AMOUNT),
                    (short) itemSection.getInt(Identifier.DATA)
            );

            final ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(Color.colorize(
                        itemSection.getString(Identifier.DISPLAY_NAME),
                        player
                ));

                meta.setLore(Color.colorize(
                        itemSection.getStringList(Identifier.LORE),
                        player
                ));
            }

            itemStack.setItemMeta(meta);

            final String action = itemSection.get(Identifier.ACTION) == null ? "none" : itemSection.getString(Identifier.ACTION);

            boolean load = true;
            switch (action.toUpperCase()) {
                case "PREVIOUS":
                    if (gui.getCurrentPageNum() <= gui.getPrevPageNum()) {
                        load = false;
                    }
                    break;
                case "NEXT":
                    if (gui.getCurrentPageNum() >= gui.getNextPageNum()) {
                        load = false;
                    }
                    break;
            }

            final GuiItem guiItem = new GuiItem(itemStack, event -> {
                switch (action.toUpperCase()) {
                    case "PREVIOUS":
                        gui.previous();
                        break;
                    case "NEXT":
                        gui.next();
                        break;
                }
            });

            if (!load) continue;
            gui.setItem(itemSection.getIntegerList(Identifier.SLOTS), guiItem);
        }
    }

}

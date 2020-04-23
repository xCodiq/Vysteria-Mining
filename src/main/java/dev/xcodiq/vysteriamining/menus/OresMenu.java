/*
 *   ~
 *   ~ Copyright 2020 NeverEndingPvP. All rights reserved.
 *   ~
 *   ~ Licensed under the NeverEndingPvP License, Version 1.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~
 *   ~ You are not allowed to edit the source.
 *   ~ You are not allowed to edit this text.
 *   ~ You are not allowed to sell this source.
 *   ~ You are not allowed to distribute this source in any way.
 *   ~ You are not allowed to claim this as yours.
 *   ~ You are not allowed to distribute.
 *   ~ You are not allowed to make own terms.
 *   ~ You are not allowed to place own warranty.
 *   ~ You are not allowed to make any sublicense.
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS.
 *   ~
 *   ~ Author: xCodiq (Discord: Codiq#3662)
 *   ~
 */

package dev.xcodiq.vysteriamining.menus;

import dev.xcodiq.vysterialibrary.library.commons.menu.InventoryClickType;
import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuItem;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.item.ItemBuilder;
import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.mine.api.MineStatistic;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import dev.xcodiq.vysteriamining.service.ActionHandler;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class OresMenu {

    public OresMenu(Player player) {
        FileConfiguration config = Mining.getInstance().getConfig();
        API api = Mining.getAPI();

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(config.getString("ores-menu.title")),
                config.getInt("ores-menu.rows")
        );

        // GLASS ITEMS
        for (int i = 0; i < menu.getInventory().getSize(); i++) {
            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(
                            Material.getMaterial(config.getString("ores-menu.glass-item.material")), 1,
                            (short) config.getInt("ores-menu.glass-item.data")
                    )
                            .setName(ChatUtils.format(config.getString("ores-menu.glass-item.displayname")))
                            .setLore(ChatUtils.format(config.getStringList("ores-menu.glass-item.lore")))
                            .toItemStack();
                }
            }, i);
        }

        // ITEMS
        for (String key : config.getConfigurationSection("ores-menu.items").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("ores-menu.items." + key);

            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                    ActionHandler.executeActions(player, section.getStringList("actions"));
                }

                @Override
                public ItemStack getItemStack() {
                    Material material = Material.getMaterial(section.getString("material"));
                    short data = (short) section.getInt("data");

                    ItemBuilder itemBuilder = new ItemBuilder(material, 1, data);

                    itemBuilder.setName(ChatUtils.format(section.getString("displayname")));
                    itemBuilder.setLore(ChatUtils.format(section.getStringList("lore").stream()
                            .map(string -> string
                                    .replace("{COAL_MINED}", "" + api.getStatistic(player.getUniqueId(), MineStatistic.COAL_ORE))
                                    .replace("{IRON_MINED}", "" + api.getStatistic(player.getUniqueId(), MineStatistic.IRON_ORE))
                                    .replace("{GOLD_MINED}", "" + api.getStatistic(player.getUniqueId(), MineStatistic.GOLD_ORE))
                                    .replace("{DIAMOND_MINED}", "" + api.getStatistic(player.getUniqueId(), MineStatistic.DIAMOND_ORE))
                                    .replace("{EMERALD_MINED}", "" + api.getStatistic(player.getUniqueId(), MineStatistic.EMERALD_ORE))
                            ).collect(Collectors.toList())));

                    return itemBuilder.toItemStack();
                }
            }, section.getInt("slot"));
        }

        menu.openMenu(player);
    }
}

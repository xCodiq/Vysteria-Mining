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

package dev.xcodiq.vysteriamining.leaderboard;

import dev.xcodiq.vysterialibrary.library.commons.menu.InventoryClickType;
import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuItem;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.item.ItemBuilder;
import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.menus.OresMenu;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import dev.xcodiq.vysteriamining.service.General;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static dev.xcodiq.vysteriamining.service.General.formatInt;

public class Leaderboard {


    private final Mining instance = Mining.getInstance();
    private final API api = Mining.getAPI();

    private final Set<String> dataSet;
    private final Instant lastUpdated = Instant.now();

    private final LinkedHashMap<UUID, Integer> serverBoard;

    public Leaderboard() {
        dataSet = instance.getDataFile().getKeys(false);

        LinkedHashMap<UUID, Integer> currentBoard = new LinkedHashMap<>();

        for (String key : dataSet) {
            UUID uuid = UUID.fromString(key);
            currentBoard.put(uuid, api.getAllStatistics(uuid));
        }

        List<Map.Entry<UUID, Integer>> list = new LinkedList<>(currentBoard.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        currentBoard.clear();

        for (Map.Entry<UUID, Integer> aList : list) {
            currentBoard.put(aList.getKey(), aList.getValue());
        }

        serverBoard = currentBoard;
    }


    public LinkedHashMap<UUID, Integer> getServerBoard() {
        return serverBoard;
    }

    public Menu getLeaderboardMenu() {
        LinkedHashMap<UUID, Integer> currentBoard = this.getServerBoard();
        ConfigurationSection section = instance.getConfig().getConfigurationSection("leaderboard");

        Menu menu = MenuAPI.getInstance().createMenu(
                ChatUtils.format(section.getString("title")
                        .replace("{TIME}", General.formatSeconds((Instant.now().toEpochMilli() / 1000) - (lastUpdated.toEpochMilli() / 1000)))),
                3
        );

        // GLASS ITEM
        for (int i = 0; i < menu.getInventory().getSize(); i++) {
            menu.addMenuItem(new MenuItem() {
                @Override
                public void onClick(Player player, InventoryClickType type) {
                }

                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(
                            Material.getMaterial(section.getString("glass-item.material")), 1,
                            (short) section.getInt("glass-item.data")
                    )
                            .setName(ChatUtils.format(section.getString("glass-item.displayname")))
                            .setLore(ChatUtils.format(section.getStringList("glass-item.lore")))
                            .toItemStack();
                }
            }, i);
        }

        List<UUID> ladder = new ArrayList<>(currentBoard.keySet());
        if (ladder.size() >= 1) this.setMenuItemByPosition(menu, 1);
        if (ladder.size() >= 2) this.setMenuItemByPosition(menu, 2);
        if (ladder.size() >= 3) this.setMenuItemByPosition(menu, 3);
        if (ladder.size() >= 4) this.setMenuItemByPosition(menu, 4);
        if (ladder.size() >= 5) this.setMenuItemByPosition(menu, 5);

        return menu;
    }

    private int getLeaderboardMenuSlot(int position) {
        switch (position + 1) {
            case 1:
                return 11;
            case 2:
                return 12;
            case 3:
                return 13;
            case 4:
                return 14;
            case 5:
                return 15;
            default:
                return 0;
        }
    }

    private void setMenuItemByPosition(Menu menu, int position) {
        LinkedHashMap<UUID, Integer> currentBoard = this.getServerBoard();

        List<UUID> ladder = new ArrayList<>(currentBoard.keySet());

        OfflinePlayer player = Bukkit.getOfflinePlayer(ladder.get(position - 1));
        ConfigurationSection section = instance.getConfig().getConfigurationSection("leaderboard");

        menu.addMenuItem(new MenuItem() {
            @Override
            public void onClick(Player player, InventoryClickType type) {
                menu.closeMenu(player);
                new OresMenu(player);
            }

            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(
                        Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()
                )
                        .setOwningPlayer(player)
                        .setName(ChatUtils.format(section.getString("item.displayname")
                                .replace("{POSITION}", "" + position)
                                .replace("{PLAYER}", player.getName())))
                        .setLore(ChatUtils.format(section.getStringList("item.lore").stream().map(string -> string
                                .replace("{TOTAL_MINED}", formatInt(currentBoard.get(ladder.get(position - 1))))
                        ).collect(Collectors.toList())))
                        .toItemStack();
            }
        }, getLeaderboardMenuSlot(position - 1));
    }
}

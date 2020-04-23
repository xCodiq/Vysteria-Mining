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

package dev.xcodiq.vysteriamining.service;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionHandler {
    public static void executeActions(Player player, String actions) {
        handle(player, Collections.singletonList(actions));
    }

    public static void executeActions(Player player, String... actions) {
        handle(player, Arrays.asList(actions));
    }

    public static void executeActions(Player player, List<String> actions) {
        handle(player, actions);
    }

    @SneakyThrows
    private static void handle(Player player, List<String> list) {
        for (String msg : list) {
            if (!msg.contains(" ")) continue;
            String actionPrefix = msg.split(" ", 2)[0].toUpperCase();
            String actionData = msg.split(" ", 2)[1];
            actionData = ChatColor.translateAlternateColorCodes('&', actionData
                    .replace("{PLAYER}", player.getName()));

            switch (actionPrefix) {
                case "[CONSOLE]":
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actionData);
                    break;

                case "[PLAYER]":
                    player.getPlayer().performCommand(actionData);
                    break;

                case "[BROADCAST]":
                    Bukkit.broadcastMessage(actionData);
                    break;

                case "[MESSAGE]":
                    player.getPlayer().sendMessage(actionData);
                    break;

                case "[CHAT]":
                    player.getPlayer().chat(actionData);
                    break;

                case "[CLOSEINVENTORY]":
                    player.getPlayer().closeInventory();
            }
        }
    }
}

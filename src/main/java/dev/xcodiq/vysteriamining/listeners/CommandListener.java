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

package dev.xcodiq.vysteriamining.listeners;

import dev.xcodiq.vysterialibrary.library.commons.menu.Menu;
import dev.xcodiq.vysterialibrary.library.utilities.collections.maps.Cache;
import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.leaderboard.Leaderboard;
import dev.xcodiq.vysteriamining.menus.OresMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.concurrent.TimeUnit;

public class CommandListener implements Listener {

    private static final FileConfiguration config = Mining.getInstance().getConfig();
    protected static Cache<Leaderboard> cache = new Cache<>(config.getInt("leaderboard.update-in-mins"), TimeUnit.MINUTES);

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().equalsIgnoreCase("/ores") ||
                event.getMessage().equalsIgnoreCase("/oresmined") ||
                event.getMessage().equalsIgnoreCase("/oresbroken")) {
            event.setCancelled(true);
            new OresMenu(player);
        } else if (event.getMessage().equalsIgnoreCase("/ores top") ||
                event.getMessage().equalsIgnoreCase("/oresmined top") ||
                event.getMessage().equalsIgnoreCase("/oresbroken top")) {
            event.setCancelled(true);

            Leaderboard leaderboard = cache.get(Leaderboard.class);

            Menu menu = leaderboard.getLeaderboardMenu();
            menu.openMenu(player);
        }
    }
}

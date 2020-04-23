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

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import dev.xcodiq.vysterialibrary.library.utilities.ChatUtils;
import dev.xcodiq.vysterialibrary.library.utilities.collections.maps.ExpiringMap;
import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.mine.RegionMine;
import dev.xcodiq.vysteriamining.mine.RegionMineLoader;
import dev.xcodiq.vysteriamining.mine.api.MineStatistic;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerMoveListener implements Listener {

    private final ExpiringMap<Player> messageCooldown = new ExpiringMap<>(10, TimeUnit.SECONDS);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        API api = Mining.getAPI();
        FileConfiguration config = Mining.getInstance().getConfig();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ())
            return;

        RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();
        if (regionContainer == null) return;

        RegionManager regionManager = regionContainer.get(to.getWorld());
        if (regionManager == null) return;

        ApplicableRegionSet applicableRegionSet = regionManager.getApplicableRegions(to);
        if (player.hasPermission("vysteria.mining.bypass")) return;

        AtomicBoolean canPass = new AtomicBoolean(true);
        applicableRegionSet.getRegions().forEach(protectedRegion -> {
            RegionMine regionMine = RegionMineLoader.getRegionMines()
                    .stream()
                    .filter(rg -> protectedRegion.getId().equalsIgnoreCase(rg.getRegionId()))
                    .findFirst().orElse(null);

            if (regionMine == null) return;

            regionMine.getRequirements().forEach(requirementMap -> requirementMap.forEach((key, value) -> {
                int currentAmount = api.getStatistic(player.getUniqueId(), MineStatistic.valueOf(key));

                if (currentAmount < value) {
                    canPass.set(false);

                    if (messageCooldown.contains(player)) return;

                    messageCooldown.push(player);
                    player.sendMessage(ChatUtils.format(config.getString("messages.cannot-enter")
                            .replace("{TYPE}", MineStatistic.valueOf(key).name().replace("_ORE", ""))
                            .replace("{AMOUNT}", String.valueOf(value - currentAmount))));
                }
            }));
        });

        if (!canPass.get()) event.setTo(from);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        API api = Mining.getAPI();
        FileConfiguration config = Mining.getInstance().getConfig();

        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ())
            return;

        RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();
        if (regionContainer == null) return;

        RegionManager regionManager = regionContainer.get(to.getWorld());
        if (regionManager == null) return;

        ApplicableRegionSet applicableRegionSet = regionManager.getApplicableRegions(to);
        if (player.hasPermission("vysteria.mining.bypass")) return;

        AtomicBoolean canPass = new AtomicBoolean(true);
        applicableRegionSet.getRegions().forEach(protectedRegion -> {
            RegionMine regionMine = RegionMineLoader.getRegionMines()
                    .stream()
                    .filter(rg -> protectedRegion.getId().equalsIgnoreCase(rg.getRegionId()))
                    .findFirst().orElse(null);

            if (regionMine == null) return;

            regionMine.getRequirements().forEach(requirementMap -> requirementMap.forEach((key, value) -> {
                int currentAmount = api.getStatistic(player.getUniqueId(), MineStatistic.valueOf(key));

                if (currentAmount < value) {
                    canPass.set(false);

                    if (messageCooldown.contains(player)) return;

                    messageCooldown.push(player);
                    player.sendMessage(ChatUtils.format(config.getString("messages.cannot-enter")
                            .replace("{TYPE}", MineStatistic.valueOf(key).name().replace("_ORE", ""))
                            .replace("{AMOUNT}", String.valueOf(value - currentAmount))));
                }
            }));
        });

        if (!canPass.get()) event.setTo(from);
    }
}

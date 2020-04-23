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
import com.sk89q.worldguard.protection.managers.RegionManager;
import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.mine.RegionMine;
import dev.xcodiq.vysteriamining.mine.RegionMineLoader;
import dev.xcodiq.vysteriamining.mine.api.MineStatistic;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Mining plugin = Mining.getInstance();
        FileConfiguration config = plugin.getConfig();

        API api = Mining.getAPI();

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();

        RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();
        if (regionContainer == null) return;

        RegionManager regionManager = regionContainer.get(block.getWorld());
        if (regionManager == null) return;

        boolean isOre = false;
        switch (material) {
            case COAL_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case GOLD_ORE:
            case IRON_ORE:
                isOre = true;
                break;
        }

        if (!isOre) return;

        int timeToRegen = config.getInt("ores-time-to-regen");

        regionManager.getApplicableRegions(block.getLocation()).forEach(protectedRegion -> {
            for (RegionMine regionMine : RegionMineLoader.getRegionMines()) {
                if (!protectedRegion.getId().equalsIgnoreCase(regionMine.getRegionId())) continue;

                api.addStatistic(player.getUniqueId(), MineStatistic.valueOf(material.name()), 1);
                api.saveItems();

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage("§c§l[!] §cYour inventory is full, items will be dropped on the ground!");
                    switch (material) {
                        case COAL_ORE:
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COAL));
                            break;
                        case DIAMOND_ORE:
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND));
                            break;
                        case EMERALD_ORE:
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.EMERALD));
                            break;
                        default:
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material));
                            break;
                    }
                } else {
                    block.getDrops().clear();
                    block.getDrops().forEach(itemStack -> player.getInventory().addItem(itemStack));
                }

                player.sendMessage("§a§l+1 " + MineStatistic.valueOf(material.name()).name().toUpperCase().replace("_", " "));

                Bukkit.getScheduler().runTaskLater(plugin, () -> block.setType(Material.BEDROCK), 2L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> block.setType(material), timeToRegen * 20);
            }
        });
    }
}

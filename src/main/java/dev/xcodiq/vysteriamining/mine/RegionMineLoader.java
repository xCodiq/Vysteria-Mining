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

package dev.xcodiq.vysteriamining.mine;

import com.google.common.collect.Lists;
import dev.xcodiq.vysteriamining.Mining;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionMineLoader {

    private static final List<RegionMine> regionMines = Lists.newArrayList();

    public RegionMineLoader(Mining plugin) {
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection section = config.getConfigurationSection("regions");

        for (String key : section.getKeys(false)) {

            RegionMine regionMine;

            String regionId = key.toLowerCase();
            String displayName = section.getString(key + ".displayname");

            List<Map<String, Integer>> requirements = new ArrayList<>();

            if (section.getConfigurationSection(key + ".requirements").getKeys(false) == null) continue;

            for (String requirement : section.getConfigurationSection(key + ".requirements").getKeys(false)) {
                Map<String, Integer> stringIntegerMap = new HashMap<>();
                stringIntegerMap.put(requirement.toUpperCase(), section.getInt(key + ".requirements." + requirement));

                requirements.add(stringIntegerMap);
            }

            regionMine = new RegionMine(regionId, displayName, requirements);
            regionMines.add(regionMine);
        }
    }

    public static List<RegionMine> getRegionMines() {
        return regionMines;
    }
}

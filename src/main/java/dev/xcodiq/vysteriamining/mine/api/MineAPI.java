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

package dev.xcodiq.vysteriamining.mine.api;

import dev.xcodiq.vysteriamining.Mining;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class MineAPI implements API {

    private final FileConfiguration data = Mining.getInstance().getDataFile();

    @Override
    public int getStatistic(UUID uuid, MineStatistic statistic) {
        return this.data.getInt(uuid.toString() + "." + statistic.name(), 0);
    }

    @Override
    public void addStatistic(UUID uuid, MineStatistic statistic, int amount) {
        this.data.set(uuid.toString() + "." + statistic.name(), this.getStatistic(uuid, statistic) + amount);
    }

    @Override
    public int getAllStatistics(UUID uuid) {
        int totalCount = 0;

        ConfigurationSection playerSection = this.data.getConfigurationSection(uuid.toString());
        for (String key : playerSection.getKeys(false)) {
            totalCount += playerSection.getInt(key);
        }

        return totalCount;
    }

    @Override
    public void setupPlayer(UUID uuid) {
        for (MineStatistic statistic : MineStatistic.values())
            this.data.set(uuid.toString() + "." + statistic.name(), 0);

    }

    @Override
    public boolean exists(UUID uuid) {
        try {
            this.data.getInt(uuid.toString() + ".IRON_ORE");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void saveItems() {
        Mining.getInstance().getData().saveConfig();
    }
}

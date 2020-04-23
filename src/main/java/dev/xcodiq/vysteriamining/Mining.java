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

package dev.xcodiq.vysteriamining;

import dev.xcodiq.vysterialibrary.library.commons.configuration.ConfigurationManager;
import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.launcher.VysteriaPlugin;
import dev.xcodiq.vysteriamining.listeners.BlockBreakListener;
import dev.xcodiq.vysteriamining.listeners.CommandListener;
import dev.xcodiq.vysteriamining.listeners.PlayerJoinListener;
import dev.xcodiq.vysteriamining.listeners.PlayerMoveListener;
import dev.xcodiq.vysteriamining.mine.RegionMineLoader;
import dev.xcodiq.vysteriamining.mine.api.MineAPI;
import dev.xcodiq.vysteriamining.mine.api.impl.API;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;

import java.util.stream.Stream;

public class Mining extends VysteriaPlugin {

    private static Mining instance = null;
    private static API api;
    private final ConfigurationManager data = new ConfigurationManager(this, "data/data.yml");

    public Mining() {
        if (instance != null) throw new IllegalStateException("Only one instance can run");
        instance = this;
    }

    public static Mining getInstance() {
        if (instance == null) throw new IllegalStateException("Instance cannot be null");
        return instance;
    }

    public static API getAPI() {
        if (api == null) api = new MineAPI();
        return api;
    }

    @Override
    public String getPluginName() {
        return "Vysteria-Mining";
    }

    @Override
    public void onStartup() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vysteria-Library")) {
            this.getLogger().info("*** VYSTERIA-LIBRARY NOT INSTALLED! DISABLING PLUGIN ***");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getLogger().info("Enabling Vysteria-Mining by xCodiq");

        // Register all the configuration files
        this.saveDefaultConfig();
        this.data.saveDefaultConfig();

        // Register the regionMines from the configuration file
        new RegionMineLoader(this);

        // Setup the api
        api = new MineAPI();
        api.saveItems();

        // Register all the listeners
        Stream.of(
                new BlockBreakListener(),
                new PlayerMoveListener(),
                new PlayerJoinListener(),
                new CommandListener()
        ).forEach(this::registerListener);

        // Unregister Menu Listener, so it won't run multiple times
        HandlerList.unregisterAll(MenuAPI.getInstance());
    }

    @Override
    public void onShutdown() {
        // Save the data file
        if (data.getConfig() != null) data.saveConfig();
    }

    public ConfigurationManager getData() {
        return data;
    }

    public FileConfiguration getDataFile() {
        return data.getConfig();
    }
}

package dev.xcodiq.vysteriamining;

import dev.xcodiq.vysterialibrary.library.commons.menu.MenuAPI;
import dev.xcodiq.vysterialibrary.library.launcher.VysteriaPlugin;
import org.bukkit.event.HandlerList;

public class Mining extends VysteriaPlugin {

    private static Mining instance = null;

    public Mining() {
        if (instance != null) throw new IllegalStateException("Only one instance can run");
        instance = this;
    }

    public static Mining getInstance() {
        if (instance == null) throw new IllegalStateException("Instance cannot be null");
        return instance;
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

        this.saveDefaultConfig();

        HandlerList.unregisterAll(MenuAPI.getInstance());
    }

    @Override
    public void onShutdown() {

    }
}

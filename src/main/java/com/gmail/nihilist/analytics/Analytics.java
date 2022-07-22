package com.gmail.nihilist.analytics;

import com.gmail.nihilist.analytics.storage.DataStorage;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Analytics extends JavaPlugin {

    private AnalyticsConfig configuration;
    private DataStorage storage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configuration = new AnalyticsConfig(this);

        try {
            storage = configuration.initializeDataStorage();
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize data storage", e);
        }

        if (storage == null) {
            getPluginLoader().disablePlugin(this);
            return;
        }

        storage.createTable();

        PluginCommand command = getCommand("analytics");
        if (command != null) {
            command.setExecutor(new AnalyticsCommand(this));
        }

        AnalyticsListener loginListener = new AnalyticsListener(this);
        getServer().getPluginManager().registerEvents(loginListener, this);
    }

    public AnalyticsConfig config() {
        return this.configuration;
    }

    public DataStorage storage() {
        return this.storage;
    }
}

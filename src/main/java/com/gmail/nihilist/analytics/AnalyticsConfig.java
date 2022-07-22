package com.gmail.nihilist.analytics;

import com.gmail.nihilist.analytics.storage.DataStorage;
import com.gmail.nihilist.analytics.storage.StorageType;
import com.gmail.nihilist.analytics.storage.impl.H2Storage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.h2.jdbcx.JdbcDataSource;

public final class AnalyticsConfig {

    private final Analytics plugin;
    private final ConfigurationSection config;

    private final String commandOutputMessage;

    AnalyticsConfig(Analytics plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        ConfigurationSection messages = config.getConfigurationSection("messages");
        this.commandOutputMessage = messages.getString("command-output");
    }

    public DataStorage initializeDataStorage() {
        StorageType type;
        try {
            type = StorageType.valueOf(config.getString("storage.type"));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Could not detect data storage set.");
        }

        if (type == StorageType.H2) {
            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName(JdbcDataSource.class.getName());
            config.addDataSourceProperty("URL", String.format("jdbc:h2:%s/database;MODE=MySQL", plugin.getDataFolder().getAbsolutePath()));

            HikariDataSource dataSource = new HikariDataSource(config);
            return new H2Storage(plugin, dataSource, "table");
        }

        return null;
    }

    public String commandOutputMessage() {
        return this.commandOutputMessage;
    }
}

package com.gmail.nihilist.analytics;

import com.gmail.nihilist.analytics.storage.DataStorage;
import com.gmail.nihilist.analytics.util.AsyncHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class AnalyticsListener implements Listener {
    private final DataStorage storage;

    public AnalyticsListener(Analytics plugin) {
        this.storage = plugin.storage();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        AsyncHelper.executor().submit(() -> storage.insert(event.getPlayer().getUniqueId(),
                event.getHostname().split(":")[0]));
    }
}

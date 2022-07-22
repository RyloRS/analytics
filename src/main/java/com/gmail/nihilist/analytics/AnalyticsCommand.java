package com.gmail.nihilist.analytics;

import com.gmail.nihilist.analytics.storage.DataStorage;
import com.gmail.nihilist.analytics.storage.model.JoinRecord;
import com.gmail.nihilist.analytics.util.AsyncHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public final class AnalyticsCommand implements CommandExecutor {
    private final AnalyticsConfig config;
    private final DataStorage storage;

    private static final String PERMISSION = "analytics.use";
    private static final String CMD_USAGE_MSG = "/analytics <hostname>";
    private static final String CMD_OUTPUT_MSG = "Stats for %s\nUnique: %s\nTotal: %s";

    public AnalyticsCommand(Analytics plugin) {
        this.config = plugin.config();
        this.storage = plugin.storage();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(PERMISSION)) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + CMD_USAGE_MSG);
                return true;
            }

            String hostname = args[0];
            CompletableFuture<JoinRecord> future;
            future = CompletableFuture.supplyAsync(() -> storage.getRecord(hostname), AsyncHelper.executor());

            future.thenAccept((record) -> {
              String message = config.commandOutputMessage();
              message = message.replace("%hostname", hostname)
                      .replace("%unique", String.valueOf(record.uniqueJoins()))
                      .replace("%total", String.valueOf(record.totalJoins()));
              sender.sendMessage(message);
            });
        }
        return true;
    }
}

package com.gmail.nihilist.analytics.storage.impl;

import com.gmail.nihilist.analytics.Analytics;
import com.gmail.nihilist.analytics.storage.DataStorage;
import com.gmail.nihilist.analytics.storage.model.DatabaseConstants;
import com.gmail.nihilist.analytics.storage.model.JoinRecord;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import static java.lang.String.format;

public class H2Storage implements DataStorage {

    private final Analytics plugin;
    private final DataSource dataSource;
    private final String table;

    public H2Storage(Analytics plugin, DataSource dataSource, String table) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.table = table;
    }

    public void createTable() {
        try (Connection con = this.dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(format(DatabaseConstants.CREATE_TABLE_STRING, this.table))) {
            ps.execute();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error occurred creating table", e);
        }
    }

    @Override
    public JoinRecord getRecord(String hostname) {
        return new JoinRecord(get(hostname, format(DatabaseConstants.SELECT_UNIQUE_STRING, this.table)),
                              get(hostname, format(DatabaseConstants.SELECT_TOTAL_STRING, this.table)));
    }

    @Override
    public void insert(UUID uuid, String hostname) {
        try (Connection con = this.dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(format(DatabaseConstants.INSERT_DATA_STRING, this.table))) {
            ps.setString(1, hostname);
            ps.setBytes(2, parseUUID(uuid));
            ps.execute();
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error occurred inserting data", e);
        }
    }

    private int get(String hostname, String statement) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setString(1, hostname);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error occurred retrieving data", e);
        }
        return 0;
    }

    private byte[] parseUUID(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }
}

package com.gmail.nihilist.analytics.storage.model;

public final class DatabaseConstants {
    public static final String CREATE_TABLE_STRING = """
            CREATE TABLE IF NOT EXISTS `%s` (
              `hostname` VARCHAR(32) NOT NULL,
              `uuid` BINARY(16) NOT NULL,
              `count` INT NOT NULL,
              PRIMARY KEY (hostname, uuid)
            )""";
    public static final String SELECT_UNIQUE_STRING =
            "SELECT COUNT(*) from `%s` WHERE hostname = ?";
    public static final String SELECT_TOTAL_STRING =
            "SELECT SUM(count) FROM `%s` WHERE hostname = ?";
    public static final String INSERT_DATA_STRING =
            "INSERT INTO `%s` (hostname, uuid, count) VALUES(?, ?, 1) ON DUPLICATE KEY UPDATE count = count + 1";
}

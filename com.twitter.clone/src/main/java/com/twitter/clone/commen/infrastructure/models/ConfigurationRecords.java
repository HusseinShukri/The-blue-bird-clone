package com.twitter.clone.commen.infrastructure.models;

public class ConfigurationRecords {
    public record AppConfig(DatabaseConfig database) {}
    public record DatabaseConfig(mariadbConfig mariadb) {}
    public record mariadbConfig(
            String jdbcUrl,
            String userName,
            String password,
            String schema,
            int minimumPoolSize,
            int maximumPoolSize) {}

    public record JavalinConfig() {}
}


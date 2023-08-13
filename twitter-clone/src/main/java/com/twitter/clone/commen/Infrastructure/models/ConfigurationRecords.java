package com.twitter.clone.commen.Infrastructure.models;

public class ConfigurationRecords {
    public record AppConfig(DatabaseConfig database) {}
    public record DatabaseConfig(mariadbConfigRecord mariadb) {}
    public record mariadbConfigRecord(
            String jdbcUrl,
            String userName,
            String password,
            String schema,
            int minimumPoolSize,
            int maximumPoolSize) {}
}


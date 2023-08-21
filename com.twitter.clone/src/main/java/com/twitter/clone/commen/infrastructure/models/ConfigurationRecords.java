package com.twitter.clone.commen.infrastructure.models;

public class ConfigurationRecords {
    public record AppConfig(DatabaseConfig Database) {}
    public record DatabaseConfig(mariadbConfig Mariadb) {}
    public record mariadbConfig(
            String JdbcUrl,
            String UserName,
            String Password,
            String Schema,
            int MinimumPoolSize,
            int MaximumPoolSize,
            int IdleTimeout,
            int MaxLifetime) {}
}


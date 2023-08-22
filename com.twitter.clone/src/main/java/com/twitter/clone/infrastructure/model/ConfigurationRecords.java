package com.twitter.clone.infrastructure.model;

public class ConfigurationRecords {
    public record AppConfig(DatabaseConfig Database,Security Security) {}
    public record DatabaseConfig(MariadbConfig Mariadb) {}
    public record MariadbConfig(
            String JdbcUrl,
            String UserName,
            String Password,
            String Schema,
            int MinimumPoolSize,
            int MaximumPoolSize,
            int IdleTimeout,
            int MaxLifetime) {}

    public record Security(JwtConfig Jwt){}
    public record JwtConfig(String SecretKey, Long ExpirationTime){}
}


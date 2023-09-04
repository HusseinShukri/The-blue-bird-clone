package com.twitter.clone.infrastructure.model;

public class ConfigurationRecords {
    public record AppConfig(App App, DatabaseConfig Database,Security Security) {}
    public record App(Integer Port){}
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
    public record Security(CookieConfig Cookie, JwtConfig Jwt){}
    public record JwtConfig(String SecretKey, Long ExpirationTime){}
    public record CookieConfig(String CookieName,String Path, int MaxAge, boolean HttpOnly ){}
}


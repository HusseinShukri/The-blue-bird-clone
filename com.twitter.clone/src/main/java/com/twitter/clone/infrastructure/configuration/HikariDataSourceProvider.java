package com.twitter.clone.infrastructure.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.clone.infrastructure.models.ConfigurationRecords;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;

@Getter
@ThreadSafe
@Singleton
public class HikariDataSourceProvider {
    private final DataSource dataSource;

    @Inject
    private HikariDataSourceProvider(ConfigurationRecords.mariadbConfig config) {
        dataSource = new HikariDataSource(getHikariConfig(config));
    }

    @NotNull
    private static HikariConfig getHikariConfig(ConfigurationRecords.mariadbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.JdbcUrl());
        hikariConfig.setUsername(config.UserName());
        hikariConfig.setPassword(config.Password());
        hikariConfig.setSchema(config.Schema());
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMinimumIdle(config.MinimumPoolSize());
        hikariConfig.setMaximumPoolSize(config.MaximumPoolSize());
        hikariConfig.setIdleTimeout(config.IdleTimeout());
        hikariConfig.setMaxLifetime(config.MaxLifetime());
        hikariConfig.addDataSourceProperty("useBulkStmts", false);
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8mb4");
        hikariConfig.addDataSourceProperty("connectionCollation", "utf8mb4_general_ci");
        return hikariConfig;
    }
}

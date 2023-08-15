package com.twitter.clone.commen.infrastructure.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.clone.commen.infrastructure.models.ConfigurationRecords;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;

@Getter
@ThreadSafe
@Singleton
public class HikariManager {
    private static HikariManager instance;
    private final DataSource dataSource;

    private HikariManager(ConfigurationRecords.mariadbConfig config) {
        dataSource = new HikariDataSource(getHikariConfig(config));
    }

    @NotNull
    private static HikariConfig getHikariConfig(ConfigurationRecords.mariadbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.jdbcUrl());
        hikariConfig.setUsername(config.userName());
        hikariConfig.setPassword(config.password());
        hikariConfig.setSchema(config.schema());
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMinimumIdle(config.minimumPoolSize());
        hikariConfig.setMaximumPoolSize(config.maximumPoolSize());
        // TODO add those to the config files
        hikariConfig.setIdleTimeout(30000L);
        hikariConfig.setMaxLifetime(1800000L);
        hikariConfig.addDataSourceProperty("useBulkStmts", false);
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8mb4");
        hikariConfig.addDataSourceProperty("connectionCollation", "utf8mb4_general_ci");
        return hikariConfig;
    }

    @Inject
    public static synchronized HikariManager getInstance(ConfigurationRecords.mariadbConfig config) {
        if (instance == null) {
            instance = new HikariManager(config);
        }
        return instance;
    }
}

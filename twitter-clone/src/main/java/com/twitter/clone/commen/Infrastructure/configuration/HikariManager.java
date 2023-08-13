package com.twitter.clone.commen.Infrastructure.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.clone.commen.Infrastructure.models.ConfigurationRecords;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Singleton
public class HikariManager {
    private static HikariManager _instance;
    private final DataSource _dataSource;

    private HikariManager(ConfigurationRecords.mariadbConfigRecord config) {
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
        hikariConfig.setMaxLifetime(20000L);
        hikariConfig.addDataSourceProperty("useBulkStmts", false);
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8mb4");
        hikariConfig.addDataSourceProperty("connectionCollation", "utf8mb4_general_ci");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        _dataSource = new HikariDataSource(hikariConfig);
    }

    @Inject
    public static HikariManager getInstance(ConfigurationRecords.mariadbConfigRecord config) {
        if (_instance == null) {
            synchronized (ConfigurationManager.class) {
                if (_instance == null) {
                    _instance = new HikariManager(config);
                }
            }
        }
        return _instance;
    }

    public DataSource getDataSource() {
        return _dataSource;
    }
}

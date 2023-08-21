package com.twitter.clone.infrastructure.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.twitter.clone.infrastructure.configuration.AppConfigProvider;
import com.twitter.clone.infrastructure.configuration.HikariDataSourceProvider;
import com.twitter.clone.infrastructure.models.ConfigurationRecords;
import com.twitter.clone.tweet.infrastructure.route.TweetRoute;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.modelmapper.ModelMapper;

public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AppConfigProvider.class).asEagerSingleton();
        bind(HikariDataSourceProvider.class).asEagerSingleton();
        bind(ModelMapper.class).asEagerSingleton();
        bind(TweetRoute.class);
    }

    @Provides
    public ConfigurationRecords.mariadbConfig provideMariadbConfig(AppConfigProvider configManager) {
        return configManager.getAppConfig().Database().Mariadb();
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(HikariDataSourceProvider hikariDataSourceProvider) {
        var dataSource = hikariDataSourceProvider.getDataSource();
        var jdbi = Jdbi.create(dataSource);
        jdbi.setSqlLogger(new Slf4JSqlLogger());
        jdbi.installPlugin(new Jackson2Plugin());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        return jdbi;
    }

    @Provides
    @Singleton
    public Flyway provideFlyway(AppConfigProvider configManager, HikariDataSourceProvider hikariDataSourceProvider) {
        var dataSource = hikariDataSourceProvider.getDataSource();
        var flyway = Flyway.configure()
                .defaultSchema(configManager.getAppConfig().Database().Mariadb().Schema())
                .locations("classpath:db/migration")
                .table("flyway_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateMigrationNaming(true)
                .target(MigrationVersion.LATEST)
                .dataSource(dataSource)
                .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }
}

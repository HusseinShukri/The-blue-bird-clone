package com.twitter.clone.module;

import com.google.inject.AbstractModule;
import com.twitter.clone.commen.infrastructure.configuration.ConfigurationManager;
import com.twitter.clone.commen.infrastructure.configuration.HikariManager;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        var appConfig = ConfigurationManager.getInstance().getAppConfig();
        bind(ConfigurationManager.class).toInstance(ConfigurationManager.getInstance());
        bind(HikariManager.class).toInstance(HikariManager.getInstance(appConfig.database().mariadb()));

        var dataSource = HikariManager.getInstance(appConfig.database().mariadb()).getDataSource();

        var jdbi = Jdbi.create(dataSource);
        jdbi.setSqlLogger(new Slf4JSqlLogger());
        jdbi.installPlugin(new Jackson2Plugin());
        jdbi.installPlugin(new SqlObjectPlugin());

        jdbi. getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        bind(Jdbi.class).toInstance(jdbi);

//        // TODO add flyway to the config files
        var flyway = Flyway.configure()
                .defaultSchema(appConfig.database().mariadb().schema())
                .locations("classpath:db/migration")
                .table("flyway_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateMigrationNaming(true)
                .target(MigrationVersion.LATEST)
                .dataSource(dataSource)
                .load();
        bind(Flyway.class).toInstance(flyway);
        flyway.repair();
        flyway.migrate();
    }
}

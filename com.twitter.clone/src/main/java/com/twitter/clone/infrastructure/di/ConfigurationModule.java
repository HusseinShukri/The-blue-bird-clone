package com.twitter.clone.infrastructure.di;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.twitter.clone.infrastructure.app.TwitterCloneApp;
import com.twitter.clone.infrastructure.app.middleware.JwtMiddleware;
import com.twitter.clone.infrastructure.cookie.UserClaim;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import com.twitter.clone.tweet.infrastructure.route.TweetRoute;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.modelmapper.ModelMapper;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ModelMapper.class).asEagerSingleton();
        bind(TweetRoute.class);
        bind(JwtMiddleware.class);
        bind(TwitterCloneApp.class).asEagerSingleton();
    }

    @Provides
    public JWTProvider provifrJwtProvider(ConfigurationRecords.JwtConfig jwtConfig) {
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.SecretKey());
        JWTVerifier verifier = JWT.require(algorithm).build();
        JWTGenerator<UserClaim> UserClaimGenerator = (user, alg) ->
                JWT.create()
                        .withClaim("Id", user.getId())
                        .withClaim("Name", user.getName())
                        .withClaim("Level", user.getLevel()).sign(alg);

        return new JWTProvider(algorithm, UserClaimGenerator, verifier);
    }

    @Provides
    public ConfigurationRecords.CookieConfig provideCookieConfig(ConfigurationRecords.AppConfig configManager) {
        return configManager.Security().Cookie();
    }

    @Provides
    public ConfigurationRecords.App provideApp(ConfigurationRecords.AppConfig configManager) {
        return configManager.App();
    }

    @Provides
    public ConfigurationRecords.JwtConfig provideJwtConfig(ConfigurationRecords.AppConfig configManager) {
        return configManager.Security().Jwt();
    }

    @Provides
    public ConfigurationRecords.MariadbConfig provideMariadbConfig(ConfigurationRecords.AppConfig configManager) {
        return configManager.Database().Mariadb();
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        var jdbi = Jdbi.create(dataSource);
        jdbi.setSqlLogger(new Slf4JSqlLogger());
        jdbi.installPlugin(new Jackson2Plugin());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
        return jdbi;
    }

    @Provides
    @Singleton
    public Flyway provideFlyway(ConfigurationRecords.AppConfig configManager, DataSource dataSource) {
        return Flyway.configure()
                .defaultSchema(configManager.Database().Mariadb().Schema())
                .locations("classpath:db/migration")
                .table("flyway_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateMigrationNaming(true)
                .target(MigrationVersion.LATEST)
                .dataSource(dataSource)
                .load();
    }

    @Provides
    @Singleton
    public DataSource providerHikariDataSource(ConfigurationRecords.MariadbConfig config) {
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
        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public ConfigurationRecords.AppConfig providerAppConfig() {
        ConfigurationRecords.AppConfig appConfig;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File configFile = new File(classLoader.getResource("configurations.json").getFile());
            try (FileInputStream fileInputStream = new FileInputStream(configFile)) {
                appConfig = new ObjectMapper().readValue(fileInputStream, ConfigurationRecords.AppConfig.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file", e);
        }

        return appConfig;
    }
}

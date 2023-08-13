package com.twitter.clone;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twitter.clone.commen.Infrastructure.configuration.ConfigurationManager;
import com.twitter.clone.commen.Infrastructure.configuration.HikariManager;
import com.twitter.clone.commen.Infrastructure.models.ConfigurationRecords;
import com.twitter.clone.module.ConfigurationModule;
import org.jdbi.v3.core.Jdbi;

public class Application {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigurationModule());

        ConfigurationManager injectorInstance = injector.getInstance(ConfigurationManager.class);
        ConfigurationRecords.AppConfig appConfig = injectorInstance.getAppConfig();
        System.out.println("Database Host: " + appConfig.database().mariadb().userName());

        var hikariManager = injector.getInstance(HikariManager.class);
        var jdbi = injector.getInstance(Jdbi.class);
        System.out.println("jdbi.toString: " + jdbi.toString());


    }
}

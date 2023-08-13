package com.twitter.clone.commen.Infrastructure.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.clone.commen.Infrastructure.models.ConfigurationRecords.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

@Singleton
public class ConfigurationManager {
    private static volatile ConfigurationManager _instance;
    private final AppConfig _appConfig;

    @Inject
    private ConfigurationManager() {
        try {
            _appConfig = new ObjectMapper().readValue(new File("twitter-clone/resource/configurations.json"), AppConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file", e);
        }
    }

    public static ConfigurationManager getInstance() {
        if (_instance == null) {
            synchronized (ConfigurationManager.class) {
                if (_instance == null) {
                    _instance = new ConfigurationManager();
                }
            }
        }
        return _instance;
    }

    public AppConfig getAppConfig() {
        return _appConfig;
    }
}

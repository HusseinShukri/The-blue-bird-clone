package com.twitter.clone.commen.infrastructure.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twitter.clone.commen.infrastructure.models.ConfigurationRecords.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Getter
@ThreadSafe
@Singleton
public class AppConfigProvider {
    private final AppConfig appConfig;

    @Inject
    private AppConfigProvider() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File configFile = new File(classLoader.getResource("configurations.json").getFile());
            try (FileInputStream fileInputStream = new FileInputStream(configFile)) {
                appConfig = new ObjectMapper().readValue(fileInputStream, AppConfig.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file", e);
        }
    }
}

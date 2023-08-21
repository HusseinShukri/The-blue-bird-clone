package com.twitter.clone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twitter.clone.infrastructure.di.ConfigurationModule;
import com.twitter.clone.tweet.infrastructure.di.TweetModule;
import com.twitter.clone.tweet.infrastructure.route.TweetRoute;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.validation.JavalinValidation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Slf4j
public class Application {

    private static Javalin javalinApp;

    public static void main(String[] args) {

        int port = 7070;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        javalinApp = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper));

            config.showJavalinBanner = true;

            config.jetty.server(() -> {
                Server server = new Server(port);
                HttpConfiguration httpConfig = new HttpConfiguration();
                httpConfig.addCustomizer(new org.eclipse.jetty.server.ForwardedRequestCustomizer());
                HttpConnectionFactory connectionFactory = new HttpConnectionFactory(httpConfig);
                ServerConnector connector = new ServerConnector(server, connectionFactory);
                connector.setPort(port);
                server.setConnectors(new ServerConnector[]{connector});
                return server;
            });

            config.requestLogger.http((ctx, executionTimeMs) -> {
                LoggerFactory.getLogger(Javalin.class).info("{} {} {} ({} ms)",
                        ctx.method(),
                        ctx.path(),
                        ctx.status(),
                        executionTimeMs
                );
            });
        });

        javalinApp.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result(e.getMessage());
        });

        javalinApp.exception(IllegalStateException.class, (ex, ctx) -> {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            ctx.json(ex.getMessage());
        });

        javalinApp.exception(Exception.class, (ex, ctx) -> {
            String message = String.format("Error Processing Request : %s : %s : %s", ctx.url(), ex.getClass().getName(), ex.getMessage());
            log.error(message);
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE);
            ctx.result(message);
        });

        JavalinValidation.register(BigDecimal.class, BigDecimal::new);

        Injector injector = Guice.createInjector(new ConfigurationModule(), new TweetModule());
        javalinApp.routes(injector.getInstance(TweetRoute.class));
        javalinApp.start();
    }
}

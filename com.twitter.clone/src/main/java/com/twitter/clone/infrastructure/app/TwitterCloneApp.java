package com.twitter.clone.infrastructure.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Injector;
import com.twitter.clone.authentication.infrastructure.route.AuthenticationRoute;
import com.twitter.clone.tweet.infrastructure.route.TweetRoute;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import io.javalin.validation.JavalinValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class TwitterCloneApp {

    private final Injector injector;
    private final int port;
    private static Javalin javalinApp;

    public void start() {
        javalinApp.start();
    }

    public void init() {
        javalinConfiguration(getObjectMapper(), port);
        exceptionConfiguration();
        javalinTypesConfiguration();
        routeConfiguration();
    }

    private static void javalinTypesConfiguration() {
        JavalinValidation.register(BigDecimal.class, BigDecimal::new);
    }

    @NotNull
    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    private void javalinConfiguration(ObjectMapper objectMapper, int port) {
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
    }

    private void exceptionConfiguration() {
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
    }

    private void routeConfiguration() {
        javalinApp.routes(injector.getInstance(TweetRoute.class));
        javalinApp.routes(injector.getInstance(AuthenticationRoute.class));
    }
}

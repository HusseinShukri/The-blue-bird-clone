package com.twitter.clone.infrastructure.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import com.twitter.clone.authentication.domain.exception.UserAlreadyExistException;
import com.twitter.clone.authentication.infrastructure.route.AuthenticationRoute;
import com.twitter.clone.infrastructure.app.middleware.JwtMiddleware;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import com.twitter.clone.newsfeed.api.model.Post;
import com.twitter.clone.newsfeed.infrastructure.route.NewsfeedRoute;
import com.twitter.clone.tweet.infrastructure.route.TweetRoute;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinPebble;
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
import java.util.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TwitterCloneApp {

    private final ConfigurationRecords.App port;
    private final JwtMiddleware jwtMiddleware;
    private final TweetRoute tweetRoute;
    private final AuthenticationRoute authenticationRoute;
    private final NewsfeedRoute newsfeedRoute;
    private static Javalin javalinApp;

    public void start() {
        javalinApp.start();
    }

    public void init() {
        javalinConfiguration(getObjectMapper(), port.Port());
        exceptionConfiguration();
        javalinTypesConfiguration();
        routeConfiguration();
        middlewareConfiguration();
        pebbleConfiguration();
    }

    private void javalinTypesConfiguration() {
        JavalinValidation.register(BigDecimal.class, BigDecimal::new);
    }

    @NotNull
    private ObjectMapper getObjectMapper() {
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

            config.staticFiles.enableWebjars();

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/static";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = false;
                staticFiles.aliasCheck = null;
                staticFiles.skipFileFunction = req -> false;
            });

        });
    }

    private void exceptionConfiguration() {
        javalinApp.exception(IllegalArgumentException.class, (ex, ctx) -> {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result(ex.getMessage());
        });

        javalinApp.exception(IllegalStateException.class, (ex, ctx) -> {
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            ctx.json(ex.getMessage());
        });

        javalinApp.exception(NoSuchElementException.class, (ex, ctx) -> {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(ex.getMessage());
        });

        javalinApp.exception(Exception.class, (ex, ctx) -> {
            String message = String.format("Error Processing Request : %s : %s : %s", ctx.url(), ex.getClass().getName(), ex.getMessage());
            log.error(message);
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE);
            ctx.result(message);
        });

        javalinApp.exception(UserAlreadyExistException.class, (ex, ctx) -> {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(ex.getMessage());
        });
    }

    private void routeConfiguration() {
        Post post1 = new Post();
        post1.setText("Hello from the other");
        post1.setName("Hussein");

        Post post2 = new Post();
        post2.setText("I must've called a thousand times\n");
        post2.setName("Musab");

        Post post3 = new Post();
        post3.setText("To tell you I'm sorry for everything that I've done\n");
        post3.setName("Qusay");

        List<Post> posts  = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        Map<String, Object> model = new HashMap<>();
        model.put("posts", posts);

        javalinApp.get("/", ctx -> {
            ctx.render("templates/newsfeed/index.html", model);
        });
        javalinApp.routes(tweetRoute);
        javalinApp.routes(authenticationRoute);
        javalinApp.routes(newsfeedRoute);
    }

    private void middlewareConfiguration() {
        javalinApp.before(jwtMiddleware);
    }

    private void pebbleConfiguration() {
        JavalinRenderer.register(new JavalinPebble(), ".html", ".peb", ".pebble");
    }
}

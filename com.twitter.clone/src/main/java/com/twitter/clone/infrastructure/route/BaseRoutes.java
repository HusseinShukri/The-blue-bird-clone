package com.twitter.clone.infrastructure.route;

import com.twitter.clone.infrastructure.annotations.route.Http;
import com.twitter.clone.infrastructure.annotations.route.RouteController;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.path;

public abstract class BaseRoutes implements EndpointGroup {
    public static final String BASE_PACKAGE = "com.twitter.clone";
    public static final String BASE_PATH = "/twitter-clone";
    public static final String PATH_SEPARATE = "/";
    protected final Map<Class<?>, Object> controllerInstancesMap = new HashMap<>();

    protected abstract void initializeControllerInstances();

    @Override
    public void addEndpoints() {
        initializeControllerInstances();

        Reflections reflections = new Reflections(BASE_PACKAGE);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(RouteController.class);

        for (Class<?> controllerClass : controllerClasses) {
            try {
                RouteController controllerAnnotation = controllerClass.getAnnotation(RouteController.class);
                String controllerPath = BASE_PATH + PATH_SEPARATE + controllerAnnotation.value();

                registerControllerRoutes(controllerClass, controllerPath, controllerInstancesMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void registerControllerRoutes(Class<?> controllerClass, String controllerPath, Map<Class<?>, Object> controllerInstancesMap) {
        Object controllerInstance = controllerInstancesMap.get(controllerClass);

        if (controllerInstance == null) {
            throw new RuntimeException("No instance found for controller: " + controllerClass.getName());
        }

        path(controllerPath + PATH_SEPARATE, () -> {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Http.Get.class)) {
                    registerGet(method, controllerInstance);
                } else if (method.isAnnotationPresent(Http.Post.class)) {
                    registerPost(method, controllerInstance);
                }
            }
        });
    }

    private static void registerGet(Method method, Object controllerInstance) {
        Http.Get get = method.getAnnotation(Http.Get.class);
        ApiBuilder.get(get.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void registerPost(Method method, Object controllerInstance) {
        Http.Post post = method.getAnnotation(Http.Post.class);
        ApiBuilder.post(post.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


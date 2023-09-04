package com.twitter.clone.infrastructure.route;

import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.PackagePath;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.path;

@Slf4j
public abstract class BaseRoute implements EndpointGroup {
    private static final String BASE_PACKAGE = "com.twitter.clone.";
    private static final String BASE_PATH = "/twitter-clone";
    private static final String PATH_SEPARATE = "/";
    protected final Map<Class<?>, Object> controllerInstancesMap = new HashMap<>();

    protected abstract void initializeControllerInstances();

    @Override
    public void addEndpoints() {
        initializeControllerInstances();

        Reflections reflections = new Reflections(BASE_PACKAGE + getPackagePath());

        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(RouteController.class)) {
            registerControllerRoutes(controllerClass,
                    getControllerPath(controllerClass.getAnnotation(RouteController.class)),
                    controllerInstancesMap);
        }
    }

    private @NotNull static String getControllerPath(RouteController controllerAnnotation) {
        return BASE_PATH + PATH_SEPARATE + controllerAnnotation.value();
    }

    private @NotNull String getPackagePath() throws RuntimeException {
        PackagePath packagePath = this.getClass().getAnnotation(PackagePath.class);
        if (packagePath == null) {
            throw new RuntimeException("Classes extending BaseRoute must be annotated with @BasePackageConfig");
        }
        return packagePath.value();
    }

    private void registerControllerRoutes(Class<?> controllerClass, String controllerPath, Map<Class<?>, Object> controllerInstancesMap) {
        Object controllerInstance = controllerInstancesMap.get(controllerClass);
        if (controllerInstance == null) {
            throw new RuntimeException("No instance found for controller: " + controllerClass.getName());
        }

        path(controllerPath + PATH_SEPARATE, () -> {
            for (Method method : controllerClass.getDeclaredMethods()) {
                try {
                    if (method.isAnnotationPresent(Http.Get.class)) {
                        registerGet(method, controllerInstance);
                    } else if (method.isAnnotationPresent(Http.Post.class)) {
                        registerPost(method, controllerInstance);
                    } else if (method.isAnnotationPresent(Http.Put.class)) {
                        registerPut(method, controllerInstance);
                    } else if (method.isAnnotationPresent(Http.Patch.class)) {
                        registerPatch(method, controllerInstance);
                    } else if (method.isAnnotationPresent(Http.Delete.class)) {
                        registerDelete(method, controllerInstance);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                   throw e;
                }
            }
        });
    }

    protected void registerGet(Method method, Object controllerInstance) {
        Http.Get get = method.getAnnotation(Http.Get.class);
        ApiBuilder.get(get.value(), ctx -> {
            method.invoke(controllerInstance, ctx);
        });
    }

    protected void registerPost(Method method, Object controllerInstance) {
        Http.Post post = method.getAnnotation(Http.Post.class);
        ApiBuilder.post(post.value(), ctx -> {
            method.invoke(controllerInstance, ctx);
        });
    }

    protected void registerPut(Method method, Object controllerInstance) {
        Http.Put put = method.getAnnotation(Http.Put.class);
        ApiBuilder.put(put.value(), ctx -> {
            method.invoke(controllerInstance, ctx);
        });
    }

    protected void registerPatch(Method method, Object controllerInstance) {
        Http.Patch patch = method.getAnnotation(Http.Patch.class);
        ApiBuilder.patch(patch.value(), ctx -> {
            method.invoke(controllerInstance, ctx);
        });
    }

    protected void registerDelete(Method method, Object controllerInstance) {
        Http.Delete delete = method.getAnnotation(Http.Delete.class);
        ApiBuilder.delete(delete.value(), ctx -> {
            method.invoke(controllerInstance, ctx);
        });
    }
}


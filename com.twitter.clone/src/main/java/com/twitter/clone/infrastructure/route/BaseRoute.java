package com.twitter.clone.infrastructure.route;

import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import org.reflections.Reflections;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.path;

public abstract class BaseRoute implements EndpointGroup {
    public static final String BASE_PACKAGE = "com.twitter.clone.";
    public static final String BASE_PATH = "/twitter-clone";
    public static final String PATH_SEPARATE = "/";
    protected final Map<Class<?>, Object> controllerInstancesMap = new HashMap<>();

    protected abstract void initializeControllerInstances();

    protected abstract String getModuleName();

    @Override
    public void addEndpoints() {
        initializeControllerInstances();

        Reflections reflections = new Reflections(BASE_PACKAGE+getModuleName());
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

    protected void registerControllerRoutes(Class<?> controllerClass, String controllerPath, Map<Class<?>, Object> controllerInstancesMap) {
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
                }else if (method.isAnnotationPresent(Http.Put.class)) {
                    registerPut(method, controllerInstance);
                }else if (method.isAnnotationPresent(Http.Patch.class)) {
                    registerPatch(method, controllerInstance);
                }else if (method.isAnnotationPresent(Http.Delete.class)) {
                    registerDelete(method, controllerInstance);
                }
            }
        });
    }

    protected void registerGet(Method method, Object controllerInstance) {
        Http.Get get = method.getAnnotation(Http.Get.class);
        ApiBuilder.get(get.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void registerPost(Method method, Object controllerInstance) {
        Http.Post post = method.getAnnotation(Http.Post.class);
        ApiBuilder.post(post.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void registerPut(Method method, Object controllerInstance) {
        Http.Put put = method.getAnnotation(Http.Put.class);
        ApiBuilder.put(put.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void registerPatch(Method method, Object controllerInstance) {
        Http.Patch patch = method.getAnnotation(Http.Patch.class);
        ApiBuilder.patch(patch.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void registerDelete(Method method, Object controllerInstance) {
        Http.Delete delete = method.getAnnotation(Http.Delete.class);
        ApiBuilder.delete(delete.value(), ctx -> {
            try {
                method.invoke(controllerInstance, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


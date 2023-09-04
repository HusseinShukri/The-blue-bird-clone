package com.twitter.clone.authentication.infrastructure.route;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.controller.AuthenticationController;
import com.twitter.clone.infrastructure.annotation.route.PackagePath;
import com.twitter.clone.infrastructure.route.BaseRoute;
import lombok.RequiredArgsConstructor;

@PackagePath("authentication")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class AuthenticationRoute extends BaseRoute {

    private final AuthenticationController authenticationController;

    @Override
    protected void initializeControllerInstances() {
        super.controllerInstancesMap.put(AuthenticationController.class, authenticationController);
    }
}

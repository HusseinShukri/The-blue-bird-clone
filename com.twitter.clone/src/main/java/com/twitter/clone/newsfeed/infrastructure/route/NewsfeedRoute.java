package com.twitter.clone.newsfeed.infrastructure.route;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotation.route.PackagePath;
import com.twitter.clone.infrastructure.route.BaseRoute;
import com.twitter.clone.newsfeed.api.controller.NewsfeedController;
import lombok.RequiredArgsConstructor;

@PackagePath("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedRoute extends BaseRoute {

    private final NewsfeedController newsfeedController;

    @Override
    protected void initializeControllerInstances() {
        this.controllerInstancesMap.put(NewsfeedController.class, newsfeedController);
    }
}

package com.twitter.clone.tweet.infrastructure.route;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotation.route.PackagePath;
import com.twitter.clone.infrastructure.route.BaseRoute;
import com.twitter.clone.tweet.api.controller.TweetController;
import lombok.RequiredArgsConstructor;

@PackagePath("tweet")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetRoute extends BaseRoute {
    private final TweetController tweetController;

    @Override
    protected void initializeControllerInstances() {
        super.controllerInstancesMap.put(TweetController.class, tweetController);
    }
}

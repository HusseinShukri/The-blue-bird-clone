package com.twitter.clone.tweet.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotations.route.RouteController;
import com.twitter.clone.infrastructure.annotations.route.Http;
import com.twitter.clone.tweet.domain.service.TweetService;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RouteController("tweet")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetController {

    private final TweetService tweetService;

    @Http.Get("{id}")
    public void getTweet(Context ctx){
        var id = Integer.parseInt(ctx.pathParam("id"));
        var projectDto = tweetService.get(id);
        if (projectDto == null) {
            ctx.status(404);
        } else {
            ctx.json(projectDto);
        }
    }
}

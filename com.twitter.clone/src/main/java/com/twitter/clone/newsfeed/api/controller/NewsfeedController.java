package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import com.twitter.clone.tweet.api.services.ITweetService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    private final IUserService userService;
    private final ITweetService tweetService;

    @Http.Get("index")
    public void index(Context context) {
        var tweets = tweetService.fetchFeedTweets();
        context.render("templates/main/index.html", Map.of("tweets", tweets));
    }

    @Http.Get("tweet-timeline")
    public void newsfeed(Context context) {
        var tweets = tweetService.fetchFeedTweets();
        context.render("templates/main/component/tweet-timeline.html", Map.of("tweets", tweets));
    }

    @Http.Get("tweet-timeline/profile")
    public void getUserTimeline(Context context) throws UnauthorizedException {
        String userId = context.attribute("Id");

        if (userId == null) {
            throw new UnauthorizedException("User ID not provided.");
        }

        var user = userService.findUser(Integer.parseInt(userId));

        var tweets = tweetService.getTweets(user.getId());

        context.render("templates/main/component/tweet-timeline.html", Map.of("tweets", tweets));
    }

    @Http.Get("tweet-timeline/{user-id}")
    public void getUserTimeLine(Context context) {
        //TODO find better validator
        try {
            int userId = context.pathParamAsClass("user-id", Integer.class).get();
            var user = userService.findUser(userId);
            var tweets = tweetService.getTweets(user.getId());
            //TODO we need to make a user profile page whenever a user views his own profile or others profile
            context.render("templates/main/component/tweet-timeline.html", Map.of("tweets", tweets));
        } catch (Exception e) {
            context.status(HttpStatus.BAD_REQUEST).result("Invalid user-id. It must be an integer.");
        }
    }
}

package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.dto.UserDto;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import com.twitter.clone.tweet.api.services.ITweetService;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    private final IUserService userService;
    private final ITweetService tweetService;

    @Http.Get("index")
    public void index(Context context) {
        var tweets = tweetService.fetchFeedTweets();
        var users = new LinkedList<UserDto>();
        context.render("templates/main/index.html", Map.of("tweets", tweets, "users", users));
    }

    @Http.Get("tweet-timeline")
    public void newsfeed(Context context) {
        var tweets = tweetService.fetchFeedTweets();
        context.render("templates/main/component/tweet-timeline.html", Map.of("tweets", tweets));
    }

    @Http.Get("tweet-timeline/profile")
    public void getUserTimeline(Context context) throws UnauthorizedException {
        var userId = Optional.ofNullable(Integer.valueOf(context.attribute("Id")))
                .orElseThrow(() -> new UnauthorizedException("User ID not provided."));

        var user = userService.findUser(userId);

        var tweets = tweetService.getTweets(user.getId());

        context.render("templates/main/component/tweet-timeline.html", Map.of("tweets", tweets));
    }

    @Http.Get("tweet-timeline/{userId}")
    public void getUserTimeLine(Context context) {

        var userId = Optional.ofNullable(context.pathParamAsClass("userId", Integer.class).get())
                .orElseThrow(() -> new IllegalArgumentException("tweetId is required integer"));

        var user = userService.findUser(userId);
        var tweets = tweetService.getTweets(user.getId());
        //TODO we need to make a user profile page whenever a user views his own profile or others profile
        context.render("templates/main/component/profile/profile.html", Map.of("tweets", tweets, "user", user));
    }
}

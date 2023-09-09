package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import com.twitter.clone.tweet.api.dto.TweetDto;
import com.twitter.clone.tweet.api.services.ITweetService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    private final IUserService userService;
    private final ITweetService tweetService;

    private Map<String, Object> model = new HashMap<>();

    private void loadTweets() {
        List<TweetDto> tweets = new LinkedList<TweetDto>();
        TweetDto post1 = new TweetDto();
        post1.setContent("Hello from the other");
        post1.setUsername("Hussein");

        TweetDto post2 = new TweetDto();
        post2.setContent("I must've called a thousand times");
        post2.setUsername("Musab");

        TweetDto post3 = new TweetDto();
        post3.setContent("To tell you I'm sorry for everything that I've done");
        post3.setUsername("Qusay");

        Random random = new Random();
        int randomNumber = random.nextInt(5, 20);
        for (int i = 0; i < randomNumber; i++) {
            tweets.add(post1);
            tweets.add(post2);
            tweets.add(post3);
        }
        model.put("tweets", tweets);
    }

    @Http.Get("index")
    public void index(Context context) {

        context.render("templates/main/index.html", model);
    }

    @Http.Get("tweet-timeline")
    public void newsfeed(Context context) {
        loadTweets();
        context.render("templates/main/component/tweet-timeline.html", model);
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

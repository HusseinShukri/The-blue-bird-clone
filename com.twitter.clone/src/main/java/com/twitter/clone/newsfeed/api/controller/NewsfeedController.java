package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.newsfeed.api.model.Tweet;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    private Map<String, Object> model = new HashMap<>();

    private void loadTweets() {
        List<Tweet> tweets = new LinkedList<Tweet>();
        Tweet post1 = new Tweet();
        post1.setText("Hello from the other");
        post1.setName("Hussein");

        Tweet post2 = new Tweet();
        post2.setText("I must've called a thousand times");
        post2.setName("Musab");

        Tweet post3 = new Tweet();
        post3.setText("To tell you I'm sorry for everything that I've done");
        post3.setName("Qusay");

        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        tweets.add(post1);
        tweets.add(post2);
        tweets.add(post3);
        model.put("tweets",tweets);
    }

    @Http.Get("index")
    public void index(Context context) {
        loadTweets();
        context.render("templates/main/index.html", model);
    }

    @Http.Get("tweet-timeline")
    public void newsfeed(Context context) {
        loadTweets();
        context.render("templates/main/component/tweet-timeline.html", model);
    }
    @Http.Get("tweet-timeline/my-user")
    public void newsfeedMyUser(Context context) {
        var userId = context.attribute("UserId");
        context.render("templates/main/component/tweet-timeline.html", model);
    }
}

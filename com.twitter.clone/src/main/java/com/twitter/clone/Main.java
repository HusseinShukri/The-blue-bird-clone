package com.twitter.clone;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twitter.clone.authentication.infrastructure.di.AuthenticationModule;
import com.twitter.clone.infrastructure.app.TwitterCloneApp;
import com.twitter.clone.infrastructure.di.ConfigurationModule;
import com.twitter.clone.newsfeed.infrastructure.di.NewsfeedModule;
import com.twitter.clone.tweet.infrastructure.di.TweetModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigurationModule(), new TweetModule(), new AuthenticationModule(), new NewsfeedModule());
        var app = injector.getInstance(TwitterCloneApp.class);
        app.init();
        app.start();
    }
}

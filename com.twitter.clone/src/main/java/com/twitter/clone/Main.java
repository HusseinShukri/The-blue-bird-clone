package com.twitter.clone;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.twitter.clone.infrastructure.app.TwitterCloneApp;
import com.twitter.clone.infrastructure.di.ConfigurationModule;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import com.twitter.clone.tweet.infrastructure.di.TweetModule;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigurationModule(), new TweetModule());
        var port = injector.getInstance(ConfigurationRecords.App.class);
        TwitterCloneApp app = new TwitterCloneApp(injector,port.Port());
        app.init();
        app.start();
    }
}

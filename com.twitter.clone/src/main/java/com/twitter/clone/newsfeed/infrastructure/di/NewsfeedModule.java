package com.twitter.clone.newsfeed.infrastructure.di;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.twitter.clone.newsfeed.api.controller.NewsfeedController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(NewsfeedController.class);
    }
}

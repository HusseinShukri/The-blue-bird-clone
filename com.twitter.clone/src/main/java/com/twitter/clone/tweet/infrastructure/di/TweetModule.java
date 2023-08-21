package com.twitter.clone.tweet.infrastructure.di;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.twitter.clone.tweet.api.mapper.TweetMapper;
import com.twitter.clone.tweet.api.services.ITweetService;
import com.twitter.clone.tweet.domain.repository.ITweetRepository;
import com.twitter.clone.tweet.domain.service.TweetService;
import com.twitter.clone.tweet.data.dao.ITweetDao;
import com.twitter.clone.tweet.data.repository.TweetRepository;
import com.twitter.clone.tweet.api.controller.TweetController;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ITweetRepository.class).to(TweetRepository.class);
        bind(ITweetService.class).to(TweetService.class);
        bind(TweetController.class);
        bind(TweetMapper.class);
    }

    @Provides
    public ITweetDao provideTweetDao(Jdbi jdbi) {
        return jdbi.onDemand(ITweetDao.class);
    }
}

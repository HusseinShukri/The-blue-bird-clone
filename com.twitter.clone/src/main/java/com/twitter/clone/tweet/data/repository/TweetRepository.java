package com.twitter.clone.tweet.data.repository;

import com.google.inject.Inject;
import com.twitter.clone.tweet.domain.entity.Tweet;
import com.twitter.clone.tweet.domain.repository.ITweetRepository;
import com.twitter.clone.tweet.data.dao.ITweetDao;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetRepository implements ITweetRepository {
    private final ITweetDao tweetDao;

    @Override
    public int insert(Tweet tweet) {
        return 0;
    }

    @Override
    public Tweet get(int id) {
        return tweetDao.getTweetById(id);
    }
}

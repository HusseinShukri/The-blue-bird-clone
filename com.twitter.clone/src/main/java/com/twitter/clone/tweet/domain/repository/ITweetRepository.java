package com.twitter.clone.tweet.domain.repository;

import com.twitter.clone.tweet.domain.entity.Tweet;

public interface ITweetRepository {

    int insert(Tweet tweet);

    Tweet get(int id);
}

package com.twitter.clone.tweet.domain.repository;

import com.twitter.clone.tweet.domain.entity.Tweet;

import java.util.List;

public interface ITweetRepository {

    int insert(Tweet tweet);

    Tweet get(int id);

    List<Tweet> getTweets(int userId);
}

package com.twitter.clone.tweet.api.services;

import com.twitter.clone.tweet.api.dto.TweetDto;

import java.util.List;

public interface ITweetService {

    int insert(TweetDto tweet);

    TweetDto get(int id);

    List<TweetDto> getTweets(int userId);
}

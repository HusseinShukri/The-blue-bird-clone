package com.twitter.clone.tweet.api.services;

import com.twitter.clone.tweet.api.dto.TweetDto;

public interface ITweetService {
    // ideally, I should replace exposing the service interface with internal messaging system
    int insert(TweetDto tweet);

    TweetDto get(int id);
}

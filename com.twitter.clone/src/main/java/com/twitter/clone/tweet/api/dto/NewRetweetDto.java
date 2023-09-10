package com.twitter.clone.tweet.api.dto;

public record NewRetweetDto(Integer userId, Integer originalTweetId, String content) { }


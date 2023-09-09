package com.twitter.clone.tweet.domain.service;

import com.google.inject.Inject;
import com.twitter.clone.tweet.api.dto.TweetDto;
import com.twitter.clone.tweet.api.mapper.TweetMapper;
import com.twitter.clone.tweet.api.services.ITweetService;
import com.twitter.clone.tweet.domain.repository.ITweetRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetService implements ITweetService {

    private final TweetMapper mapper;
    private final ITweetRepository tweetRepository;

    @Override
    public int insert(TweetDto tweet) {
        return 0;
    }

    @Override
    public TweetDto get(int id) {
        var tweet = tweetRepository.get(id);
        return mapper.tweetToTweetDto(tweet);
    }

    @Override
    public List<TweetDto> getTweets(int userId) {
        var tweets = tweetRepository.getTweets(userId);
        return mapper.tweetsToTweetDtoList(tweets);
    }
}

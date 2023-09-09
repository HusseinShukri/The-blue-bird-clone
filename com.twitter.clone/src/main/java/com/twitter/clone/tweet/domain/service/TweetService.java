package com.twitter.clone.tweet.domain.service;

import com.google.inject.Inject;
import com.twitter.clone.tweet.api.dto.NewTweetDto;
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
    public void insert(NewTweetDto newTweet) {
        //TODO try add a feedback on the tweet insert
        var tweet = mapper.newTweetDtoToTweet(newTweet);
        tweetRepository.insert(tweet);
    }

    @Override
    public TweetDto get(int id) {
        var tweet = tweetRepository.get(id);
        return mapper.tweetToTweetDto(tweet);
    }

    @Override
    public List<TweetDto> getTweets(int userId) {
        var tweets = tweetRepository.getTweets(userId);
        return mapper.domainTweetDtoToTweetDtoList(tweets);
    }

    @Override
    public List<TweetDto> fetchFeedTweets() {
        var tweets = tweetRepository.fetchFeedTweets();
        return mapper.domainTweetDtoToTweetDtoList(tweets);
    }
}

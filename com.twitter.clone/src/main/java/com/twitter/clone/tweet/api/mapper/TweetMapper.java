package com.twitter.clone.tweet.api.mapper;

import com.google.inject.Inject;
import com.twitter.clone.tweet.api.dto.TweetDto;
import com.twitter.clone.tweet.domain.entity.Tweet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetMapper {

    private final ModelMapper mapper;

    public TweetDto tweetToTweetDto(Tweet source) {
        if (source == null) return null;
        return mapper.map(source, TweetDto.class);
    }

    public Tweet tweetDtoToTweet(TweetDto source) {
        if (source == null) return null;
        return mapper.map(source, Tweet.class);
    }

    public List<TweetDto> tweetsToTweetDtoList(List<Tweet> tweets) {
        if (tweets == null || tweets.isEmpty()) {
            return Collections.emptyList();
        }
        return tweets
                .stream()
                .map(tweet -> mapper.map(tweet, TweetDto.class))
                .collect(Collectors.toList());
    }
}

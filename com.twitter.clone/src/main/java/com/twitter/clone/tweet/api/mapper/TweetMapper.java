package com.twitter.clone.tweet.api.mapper;

import com.google.inject.Inject;
import com.twitter.clone.tweet.api.dto.NewTweetDto;
import com.twitter.clone.tweet.api.dto.TweetDto;
import com.twitter.clone.tweet.domain.entity.Tweet;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetMapper {

    private final ModelMapper mapper;

    public NewTweetDto contextToNewTweetDto(Context context, Integer id) {
        if (context == null) return null;
        return new NewTweetDto(id, context.formParam("tweetContent"));
    }

    public Tweet newTweetDtoToTweet(NewTweetDto source) {
        if (source == null) return null;
        Tweet tweet = new Tweet();
        tweet.setUserId(source.userId());
        tweet.setContent(source.content());
        return tweet;
    }

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

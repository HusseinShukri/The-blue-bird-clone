package com.twitter.clone.tweet.api.mapper;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import com.twitter.clone.tweet.api.dto.NewRetweetDto;
import com.twitter.clone.tweet.api.dto.NewTweetDto;
import com.twitter.clone.tweet.api.dto.TweetDto;
import com.twitter.clone.tweet.domain.entity.Tweet;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetMapper {

    private final ModelMapper mapper;

    public NewRetweetDto contextToNewRetweetDto(Context context) throws UnauthorizedException {
        if (context == null) {
            return null;
        }

        var originalTweetId = Optional.ofNullable(context.pathParamAsClass("tweetId", Integer.class).get())
                .orElseThrow(() -> new IllegalArgumentException("tweetId is required integer"));

        var userId = Optional.ofNullable(Integer.valueOf(context.attribute("Id")))
                .orElseThrow(() -> new UnauthorizedException("User ID not provided."));

        String tweetContent = Optional.ofNullable(context.formParam("retweetContent"))
                .filter(content -> !content.isEmpty())
                .filter(content -> !content.isBlank())
                .filter(content -> !(content.length() > 500))
                .orElseThrow(() -> new IllegalArgumentException("Content is required"));

        return new NewRetweetDto(userId, originalTweetId, tweetContent);
    }

    public NewTweetDto contextToNewTweetDto(Context context, Integer userId) {
        if (context == null) {
            return null;
        }

        String tweetContent = Optional.ofNullable(context.formParam("tweetContent"))
                .filter(content -> !content.isEmpty())
                .filter(content -> !content.isBlank())
                .filter(content -> content.length() > 500)
                .orElseThrow(() -> new IllegalArgumentException("Content is required"));

        return new NewTweetDto(userId, tweetContent);
    }

    public Tweet newTweetDtoToTweet(NewTweetDto source) {
        if (source == null) return null;
        Tweet tweet = new Tweet();
        tweet.setUserId(source.userId());
        tweet.setContent(source.content());
        return tweet;
    }

    public Tweet NewRetweetDtoToTweet(NewRetweetDto source) {
        if (source == null) return null;
        Tweet tweet = new Tweet();
        tweet.setUserId(source.userId());
        tweet.setContent(source.content());
        tweet.setOriginalTweetId(source.originalTweetId());
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

    public List<TweetDto> domainTweetDtoToTweetDtoList(List<com.twitter.clone.tweet.data.Dto.TweetDto> tweets) {
        if (tweets == null || tweets.isEmpty()) {
            return Collections.emptyList();
        }
        return tweets
                .stream()
                .map(tweet -> mapper.map(tweet, TweetDto.class))
                .collect(Collectors.toList());
    }
}

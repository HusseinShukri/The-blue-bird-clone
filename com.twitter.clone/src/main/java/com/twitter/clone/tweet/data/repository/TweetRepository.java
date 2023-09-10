package com.twitter.clone.tweet.data.repository;

import com.google.inject.Inject;
import com.twitter.clone.tweet.data.Dto.TweetDto;
import com.twitter.clone.tweet.domain.entity.Tweet;
import com.twitter.clone.tweet.domain.repository.ITweetRepository;
import com.twitter.clone.tweet.data.dao.ITweetDAO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TweetRepository implements ITweetRepository {
    private final ITweetDAO tweetDao;

    @Override
    public int insert(Tweet tweet) {
        tweetDao.insert(tweet.getUserId(),tweet.getContent());
        return 0;
    }

    @Override
    public Tweet get(int id) {
        return tweetDao.getTweetById(id);
    }

    @Override
    public List<TweetDto> getTweets(int userId) {
        return tweetDao.getTweetsByUserId(userId);
    }

    @Override
    public List<TweetDto> fetchFeedTweets() {
        return tweetDao.fetchFeedTweets();
    }

    @Override
    public void insertRetweet(Tweet tweet) {
        tweetDao.insertRetweet(tweet.getUserId(),tweet.getContent(),tweet.getOriginalTweetId());
    }
}

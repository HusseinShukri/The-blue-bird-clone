package com.twitter.clone.tweet.data.dao;

import com.twitter.clone.tweet.data.Dto.TweetDto;
import com.twitter.clone.tweet.data.dao.mapper.TweetDtoMapper;
import com.twitter.clone.tweet.domain.entity.Tweet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ITweetDAO {

    @SqlUpdate("""
            INSERT INTO `Tweet` (user_id, content, created_at, is_deleted)
            VALUES (:userId, :content, CURRENT_TIMESTAMP(), 0);
            """)
    void insert(@Bind("userId") Integer userId, @Bind("content") String content);

    @SqlQuery("""
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_deleted
                FROM
                    Tweet as t
                WHERE
                    t.is_deleted = 0
                   AND t.id = :id;
            """)
    @RegisterBeanMapper(Tweet.class)
    Tweet getTweetById(@Bind("id") long id);

    @SqlQuery("""
                SELECT
                    t.id,
                    t.user_id,
                    t.content,
                    t.created_at,
                    t.is_deleted,
                    u.username
                FROM
                    Tweet as t
                JOIN 
                    User as u on u.id = t.user_id
                WHERE 
                    t.is_deleted = 0
                   AND t.user_id = :userId
                ORDER BY t.created_at DESC;
            """)
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> getTweetsByUserId(@Bind("userId") int userId);

    @SqlQuery("""
            SELECT t.id,
                   t.user_id,
                   t.content,
                   t.created_at,
                   t.is_deleted,
                   u.username,
                   ot.id,
                   ot.user_id,
                   ot.content,
                   ot.created_at,
                   ot.is_deleted,
                   ou.username
            FROM Tweet AS t
                     JOIN
                 User AS u ON u.id = t.user_id
                     LEFT JOIN
                 Tweet AS ot ON t.original_tweet_id = ot.id AND ot.is_deleted = 0
                     LEFT JOIN
                 User AS ou ON ou.id = ot.user_id
            WHERE t.is_deleted = 0
            ORDER BY t.created_at DESC;
                """)
    @RegisterRowMapper(TweetDtoMapper.class)
    List<TweetDto> fetchFeedTweets();

    @SqlUpdate("""
            INSERT INTO `Tweet` (original_tweet_id, user_id, content, created_at, is_deleted)
            VALUES (:originalTweetId, :userId, :content, CURRENT_TIMESTAMP(), 0);
            """)
    void insertRetweet(@Bind("userId") Integer userId, @Bind("content") String content, @Bind("originalTweetId") Integer originalTweetId);
}

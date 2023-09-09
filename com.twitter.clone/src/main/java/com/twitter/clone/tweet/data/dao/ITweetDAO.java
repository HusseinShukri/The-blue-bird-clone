package com.twitter.clone.tweet.data.dao;

import com.twitter.clone.tweet.data.Dto.TweetDto;
import com.twitter.clone.tweet.domain.entity.Tweet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
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
                    t.id = :id;
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
                    t.user_id = :userId
                ORDER BY t.created_at DESC;
            """)
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> getTweetsByUserId(@Bind("userId") int userId);

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
                ORDER BY t.created_at DESC;
            """)
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> fetchFeedTweets();
}

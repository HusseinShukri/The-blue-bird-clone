package com.twitter.clone.tweet.data.dao;

import com.twitter.clone.tweet.domain.entity.Tweet;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface ITweetDAO {

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
                    t.is_deleted
            FROM
                Tweet as t
            WHERE
                t.user_id = :userId
            """)
    @RegisterBeanMapper(Tweet.class)
    List<Tweet> getTweetsByUserId(@Bind("userId") int userId);
}

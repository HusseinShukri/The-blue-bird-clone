package com.twitter.clone.tweet.data.dao.mapper;

import com.twitter.clone.tweet.data.Dto.TweetDto;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TweetDtoMapper implements RowMapper<TweetDto> {

    @Override
    public TweetDto map(ResultSet rs, StatementContext ctx) throws SQLException {
        TweetDto tweet = new TweetDto();
        tweet.setId(rs.getInt("t.id"));
        tweet.setUserId(rs.getInt("t.user_id"));
        tweet.setContent(rs.getString("t.content"));
        tweet.setCreatedAt(rs.getTimestamp("t.created_at").toLocalDateTime());
        tweet.setDeleted(rs.getBoolean("t.is_deleted"));
        tweet.setUsername(rs.getString("u.username"));
        ResultSetMetaData metaData = rs.getMetaData();

        try {
            originalTweet(rs, tweet);
        }catch (SQLException e){
            // do nothing and swallow the exception for the moment
            //TODO find a better way to handel the sql exception
            // that throws when a column name do not exists
        }
        return tweet;
    }

    private static void originalTweet(ResultSet rs, TweetDto tweet) throws SQLException {
        if (rs.getObject("ot.Id") != null) {
            TweetDto originalTweet = new TweetDto();
            originalTweet.setId(rs.getInt("ot.id"));
            originalTweet.setUserId(rs.getInt("ot.user_id"));
            originalTweet.setContent(rs.getString("ot.content"));
            originalTweet.setCreatedAt(rs.getTimestamp("ot.created_at").toLocalDateTime());
            originalTweet.setDeleted(rs.getBoolean("ot.is_deleted"));
            originalTweet.setUsername(rs.getString("ou.username"));
            tweet.setOriginalTweet(originalTweet);
        }
    }
}


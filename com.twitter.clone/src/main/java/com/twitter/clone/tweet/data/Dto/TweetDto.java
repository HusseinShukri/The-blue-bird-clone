package com.twitter.clone.tweet.data.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jdbi.v3.core.mapper.Nested;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TweetDto {
    private Integer id;
    private Integer userId;
    private String username;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private boolean isDeleted;
    @Nested
    private TweetDto originalTweet;
}

package com.twitter.clone.tweet.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class TweetDto {
    private int id;
    private int userId;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}

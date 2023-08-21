package com.twitter.clone.tweet.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Tweet {
    private int id;
    private int userId;
    private String content;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}

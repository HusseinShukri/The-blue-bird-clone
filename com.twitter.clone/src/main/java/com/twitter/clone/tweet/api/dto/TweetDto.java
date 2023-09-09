package com.twitter.clone.tweet.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class TweetDto {
    private Integer id;
    private Integer userId;
    private String name;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}

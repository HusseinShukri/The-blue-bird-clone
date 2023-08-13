package com.twitter.clone.tweet.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Tweet {
    private int Id;
    private int UserId;
    @Nullable
    private int RetweetedFrom;
    private String content;
    private LocalDateTime CreatedAt;
    private boolean IsDeleted = false;
}
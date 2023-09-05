package com.twitter.clone.newsfeed.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
    private String image;
    private String name;
    private String text;
    private int userId;
}

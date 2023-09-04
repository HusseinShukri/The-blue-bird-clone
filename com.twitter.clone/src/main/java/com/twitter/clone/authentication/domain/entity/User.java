package com.twitter.clone.authentication.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private boolean isDeleted;
}

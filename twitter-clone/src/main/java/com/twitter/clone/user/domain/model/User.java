package com.twitter.clone.user.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private int Id;
    private String Email;
    private String UserName;
    private String Password;
    private LocalDateTime CreatedAt;
    private boolean IsDeleted = false;
}

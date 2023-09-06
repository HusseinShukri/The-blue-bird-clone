package com.twitter.clone.infrastructure.cookie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserClaim {
    private String Id;
    private String name;
    private String level;
}

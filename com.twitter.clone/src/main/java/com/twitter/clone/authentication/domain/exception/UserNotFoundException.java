package com.twitter.clone.authentication.domain.exception;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    private static String Message = "User not found";

    public UserNotFoundException(){
        super(Message);
    }

    public UserNotFoundException(@Nullable String message) {
        super(message);
    }
}

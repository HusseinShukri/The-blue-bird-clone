package com.twitter.clone.authentication.domain.exception;

import javax.annotation.Nullable;

public class UserAlreadyExistException  extends Exception{

    private static String Message = "User already exist";

    public UserAlreadyExistException(){
        super(Message);
    }

    public UserAlreadyExistException(@Nullable String message) {
        super(message);
    }
}

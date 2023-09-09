package com.twitter.clone.authentication.domain.exception;

import javax.annotation.Nullable;

public class InvalidCredentialException extends Exception{
    private static String Message = "Invalid email or password";

    public InvalidCredentialException(){
        super(Message);
    }

    public InvalidCredentialException(@Nullable String message) {
        super(message);
    }
}

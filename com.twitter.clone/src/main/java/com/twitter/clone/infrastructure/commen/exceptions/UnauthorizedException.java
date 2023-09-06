package com.twitter.clone.infrastructure.commen.exceptions;

import javax.annotation.Nullable;

public class UnauthorizedException extends Exception{

    private static String Message = "Unauthorized";

    public UnauthorizedException(){
        super(Message);
    }

    public UnauthorizedException(@Nullable String message) {
        super(message);
    }
}

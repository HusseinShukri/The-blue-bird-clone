package com.twitter.clone.authentication.api.mapper;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.dto.AuthenticationDto;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class AuthenticationMapper {

    private final ModelMapper mapper;

    public AuthenticationDto.LoginDto contextToLoginDto(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        String email = Optional.ofNullable(context.formParam("email"))
                .orElseThrow(() -> new IllegalArgumentException("Email is required"));
        String password = Optional.ofNullable(context.formParam("password"))
                .orElseThrow(() -> new IllegalArgumentException("Password is required"));

        return new AuthenticationDto.LoginDto(email, password);
    }

    public AuthenticationDto.SignupDto contextToSignupDto(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        //TODO add a validation form to return to the user instead of message only
        String email = Optional.ofNullable(context.formParam("email"))
                .orElseThrow(() -> new IllegalArgumentException("Email is required"));
        String username = Optional.ofNullable(context.formParam("username"))
                .orElseThrow(() -> new IllegalArgumentException("Username is required"));
        String password = Optional.ofNullable(context.formParam("password"))
                .orElseThrow(() -> new IllegalArgumentException("Password is required"));

        return new AuthenticationDto.SignupDto(email, username, password);
    }

}

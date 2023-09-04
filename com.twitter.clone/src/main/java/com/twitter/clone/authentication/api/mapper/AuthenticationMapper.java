package com.twitter.clone.authentication.api.mapper;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.dto.AuthenticationDto;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class AuthenticationMapper {

    private final ModelMapper mapper;

    public AuthenticationDto.LoginDto contextToLoginDto(Context context){
        if (context == null) return null;
        return new AuthenticationDto.LoginDto(context.formParam("email"),context.formParam("password"));
    }

    public AuthenticationDto.SignupDto contextToSignupDto(Context context){
        if (context == null) return null;
        return new AuthenticationDto.SignupDto(context.formParam("email"), context.formParam("username"),context.formParam("password"));
    }
}

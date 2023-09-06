package com.twitter.clone.authentication.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.mapper.AuthenticationMapper;
import com.twitter.clone.authentication.api.servcie.ICookieService;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.authentication.domain.exception.UserAlreadyExistException;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.infrastructure.commen.enums.UserLevel;
import com.twitter.clone.infrastructure.cookie.UserClaim;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import javalinjwt.JWTProvider;
import lombok.RequiredArgsConstructor;

@RouteController("authentication")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class AuthenticationController {
    private final IUserService userService;
    private final ICookieService cookieService;
    private final JWTProvider JWTProvider;
    private final AuthenticationMapper authenticationMapper;

    @Http.Get("login")
    public void loginPage(Context context) {
        context.render("templates/login/index.html");
    }

    @Http.Post("login")
    public void login(Context context) {
        var loginDto = authenticationMapper.contextToLoginDto(context);
        if (loginDto == null) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        // TODO we should refactor this and replace it with exception not handle it manually
        // this will require a global handler
        var user = userService.validateLogin(loginDto);
        if (user == null) {
            context.status(HttpStatus.BAD_REQUEST).result("Invalid email or password");
            return;
        }

        UserClaim userClaim = new UserClaim(user.getId().toString(), user.getUsername(), UserLevel.USER.toString());
        context.cookie(cookieService.CreteJwtCookie(JWTProvider.generateToken(userClaim)));
        context.status(HttpStatus.PERMANENT_REDIRECT);
        context.header("hx-redirect", "/twitter-clone/newsfeed/index");
    }

    @Http.Post("signup")
    public void signup(Context context) {
        var signupDto = authenticationMapper.contextToSignupDto(context);
        if (signupDto == null) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }
        try {
            var user = userService.createNewUser(signupDto);
        } catch (UserAlreadyExistException e) {
            context.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
            return;
        }
        context.status(HttpStatus.OK).result("signup successful");
    }
}

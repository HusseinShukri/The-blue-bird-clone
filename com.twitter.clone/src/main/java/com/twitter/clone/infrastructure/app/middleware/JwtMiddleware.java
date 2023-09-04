package com.twitter.clone.infrastructure.app.middleware;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import javalinjwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class JwtMiddleware implements Handler {

    private final ConfigurationRecords.CookieConfig cookieConfig;
    private final JWTProvider jwtProvider;

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        //TODO Refactor this area to use more suitable way for excluding specific routes
        // Exclude the login route from token validation
//        if (ctx.path().equals("/twitter-clone/authentication/login")
//                || ctx.path().contains("/twitter-clone/authentication/")
//                || ctx.path().contains("/imag/")) {
//            return;
//        }
//
//        String jwtToken = ctx.cookie(cookieConfig.CookieName());
//
//        if (jwtToken == null || !isValid(jwtToken)) {
//            ctx.status(HttpStatus.UNAUTHORIZED).result("Unauthorized");
//            ctx.redirect("/twitter-clone/authentication/login");
//        }
    }

    private boolean isValid(String jwtToken) {
        try {
            jwtProvider.validateToken(jwtToken);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}

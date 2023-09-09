package com.twitter.clone.authentication.domain.service;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.servcie.ICookieService;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import io.javalin.http.Cookie;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class CookieService implements ICookieService {

    private final ConfigurationRecords.CookieConfig cookieConfig;

    @Override
    public Cookie CreateJwtCookie(String jwtToken)
    {
        Cookie cookie = new Cookie(cookieConfig.CookieName(), jwtToken);
        cookie.setMaxAge(cookieConfig.MaxAge());
        cookie.setHttpOnly(cookieConfig.HttpOnly());
        cookie.setPath(cookieConfig.Path());
        return cookie;
    }
}

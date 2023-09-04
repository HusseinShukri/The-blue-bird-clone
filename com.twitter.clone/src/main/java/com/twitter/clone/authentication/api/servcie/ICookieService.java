package com.twitter.clone.authentication.api.servcie;

import io.javalin.http.Cookie;

public interface ICookieService {
    Cookie CreteJwtCookie(String jwtToken);
}

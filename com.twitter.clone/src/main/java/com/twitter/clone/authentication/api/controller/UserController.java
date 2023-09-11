package com.twitter.clone.authentication.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RouteController("user")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))

public class UserController {

    private final IUserService userService;

    @Http.Post("search")
    public void searchUsers(Context context) throws UnauthorizedException {
        var userId = Optional.ofNullable(Integer.valueOf(context.attribute("Id")))
                .orElseThrow(() -> new UnauthorizedException("User ID not provided."));

        String searchInput = Optional.ofNullable(context.formParam("searchInput"))
                .filter(content -> !content.isEmpty())
                .filter(content -> !content.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Content is required"));

        var user = userService.findUser(userId);

        var users = userService.search(searchInput);

        context.render("templates/main/component/search/dropdownList.html", Map.of("users", users));
    }
}

package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    @Http.Get("index")
    public void index(Context context){
        context.render("templates/newsfeed/index.peb");
    }
}

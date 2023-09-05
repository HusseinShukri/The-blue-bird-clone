package com.twitter.clone.newsfeed.api.controller;

import com.google.inject.Inject;
import com.twitter.clone.infrastructure.annotation.route.Http;
import com.twitter.clone.infrastructure.annotation.route.RouteController;
import com.twitter.clone.newsfeed.api.model.Post;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RouteController("newsfeed")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class NewsfeedController {

    @Http.Get("index")
    public void index(Context context){

        Post post1 = new Post();
        post1.setText("Hello from the other");
        post1.setName("Hussein");

        List<Post> posts  = new ArrayList<>();
        posts.add(post1);
        Map<String, Object> model = new HashMap<>();
        model.put("posts", posts);
        context.render("templates/newsfeed/index.html",model);
    }
}

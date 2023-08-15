package com.twitter.clone;

import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);
    }
}

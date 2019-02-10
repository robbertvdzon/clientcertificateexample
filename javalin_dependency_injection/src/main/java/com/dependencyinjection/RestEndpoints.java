package com.dependencyinjection;

import io.javalin.Javalin;

import javax.inject.Inject;

public class RestEndpoints {

    private Javalin app;

    @Inject
    public RestEndpoints(Javalin app) {
        this.app = app;
    }

    public void init() {
        app.get("/", ctx -> ctx.result("Hello World from javalin"));
        app.get("/json", ctx -> ctx.json(new MyJson("Hello World")));
        app.post("/json", ctx -> ctx.result("Got post of " + ctx.bodyAsClass(MyJson.class).getText()));
    }


}

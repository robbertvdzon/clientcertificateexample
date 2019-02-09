package com.demo;

import io.javalin.Javalin;

public class JavalinApplication1 {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.get("/", ctx -> ctx.result("Hello World from javalin"));
        app.get("/json", ctx -> ctx.json(new MyJson("Hello world")));
        app.post("/json", ctx -> ctx.result("Got post of "+ctx.bodyAsClass(MyJson.class).getText()));
        app.start(8080);
    }
}

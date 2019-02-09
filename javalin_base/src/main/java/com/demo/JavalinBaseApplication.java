package com.demo;

import io.javalin.Javalin;

public class JavalinBaseApplication {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        new RestEndpoints().initRestEndpoints(app);
        app.start(8080);
    }
}

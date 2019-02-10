package com.dependencyinjection;

import io.javalin.Javalin;

public class DependencyInjectionApplication {
    Javalin app;

    public static void main(String[] args) {
        new DependencyInjectionApplication().start();
    }

    public void start(){
        app = Javalin.create();
        new RestEndpoints().initRestEndpoints(app);
        app.start(8080);
    }

    public void stop(){
        app.stop();
    }
}

package com.properties;

import io.javalin.Javalin;

public class PropertiesApplication {
    Javalin app;

    public static void main(String[] args) {
        new PropertiesApplication().start();
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

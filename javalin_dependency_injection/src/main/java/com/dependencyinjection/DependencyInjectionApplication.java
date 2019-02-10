package com.dependencyinjection;

import com.dependencyinjection.dagger.ComponentFactory;
import io.javalin.Javalin;

public class DependencyInjectionApplication {

    Javalin app;

    public static void main(String[] args) {
        new DependencyInjectionApplication().start();
    }

    public void start() {
        ComponentFactory componentFactory = new ComponentFactory();
        app = componentFactory.factory().buildJavalin();
        RestEndpoints restEndpoints = componentFactory.factory().buildRestEndpoints();

        restEndpoints.init();

        app.start(8080);
    }

    public void stop() {
        app.stop();
    }
}

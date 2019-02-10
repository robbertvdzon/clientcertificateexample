package com.dependencyinjection;

import io.javalin.Javalin;

public class DependencyInjectionApplication {
    Javalin app;

    public static void main(String[] args) {
        new DependencyInjectionApplication().start();
    }

    public void start(){
        MyModuleComponent myModuleComponent = DaggerMyModuleComponent.create();
        app = myModuleComponent.buildJavalin();
        RestEndpoints restEndpoints = myModuleComponent.buildRestEndpoints();
        restEndpoints.init();

        app.start(8080);
    }

    public void stop(){
        app.stop();
    }
}

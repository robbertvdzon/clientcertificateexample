package com.dependencyinjection.dagger;

import com.dependencyinjection.RestEndpoints;
import dagger.Component;
import io.javalin.Javalin;

import javax.inject.Singleton;

@Singleton
@Component(modules = MyModule.class)
public interface MyModuleComponent {
    Javalin buildJavalin();

    RestEndpoints buildRestEndpoints();
}

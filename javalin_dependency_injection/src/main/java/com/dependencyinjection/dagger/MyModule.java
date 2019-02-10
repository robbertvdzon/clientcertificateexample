package com.dependencyinjection.dagger;

import com.dependencyinjection.RestEndpoints;
import dagger.Module;
import dagger.Provides;
import io.javalin.Javalin;

import javax.inject.Singleton;

@Module
public class MyModule {

    @Provides
    @Singleton
    public static Javalin getJavalin() {
        return Javalin.create();
    }

    @Provides
    @Singleton
    public static RestEndpoints getRestEndpoints(Javalin app) {
        return new RestEndpoints(app);
    }

}

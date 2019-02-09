package com.demohttpswithclient;

import com.demohttpswithclient.ssl.SslManager;
import io.javalin.Javalin;

public class JavalinHttpsWithClientCertApplication {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        new RestEndpoints().initRestEndpoints(app);
        new SslManager().init(app);
        app.start(8080);
    }
}

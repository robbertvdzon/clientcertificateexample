package com.vdzon.javalin.server1;

import io.javalin.Javalin;

public class Application {

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        UserManagement userManagement = new UserManagement();
        new BasicAuthManager(app, userManagement);
        new RestEndpoints(app);

        app.start(7000);
    }


}

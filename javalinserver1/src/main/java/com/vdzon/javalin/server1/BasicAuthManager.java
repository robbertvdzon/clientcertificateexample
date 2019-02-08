package com.vdzon.javalin.server1;

import io.javalin.BasicAuthCredentials;
import io.javalin.Context;
import io.javalin.Javalin;

public class BasicAuthManager {


    private UserManagement userManagement;

    public BasicAuthManager(Javalin app, UserManagement userManagement){
        this.userManagement = userManagement;
        // Set the access-manager that Javalin should use
        app.accessManager((handler, ctx, permittedRoles) -> {
            MyRole userRole = getUserRole(ctx);
            if (permittedRoles.isEmpty() || permittedRoles.contains(userRole)) {
                handler.handle(ctx);
            } else {
                ctx.header("WWW-Authenticate","Basic realm=\"User Visible Realm\"");
                ctx.status(401).result("Unauthorized");
            }
        });
    }

    MyRole getUserRole(Context ctx) {
        BasicAuthCredentials basicAuthCredentials = ctx.basicAuthCredentials();
        return userManagement
                .getUsers()
                .stream()
                .filter(u->authenticateUser(u, basicAuthCredentials))
                .map(u->u.getRole())
                .findFirst()
                .orElseGet(()->MyRole.NONE);
    }

    private boolean authenticateUser(User u, BasicAuthCredentials basicAuthCredentials) {
        if (basicAuthCredentials==null) return false;
        return ("user".equals(basicAuthCredentials.getUsername()) && "passwd".equals(basicAuthCredentials.getPassword()));

    }

}

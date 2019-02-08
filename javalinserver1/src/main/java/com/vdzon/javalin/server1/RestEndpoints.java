package com.vdzon.javalin.server1;

import io.javalin.Javalin;

import static io.javalin.security.SecurityUtil.roles;

public class RestEndpoints {

    public RestEndpoints(Javalin app){
        initRestEndpoins(app);
    }

    private void initRestEndpoins(Javalin app){
        app.get("/insecure", ctx -> ctx.result("Insecured page"));
        app.get("/secure", ctx -> ctx.result("Secured page").header("WWW-Authenticate", "Basic realm=\"User Visible Realm\""), roles(MyRole.ROLE_ONE));
    }
}

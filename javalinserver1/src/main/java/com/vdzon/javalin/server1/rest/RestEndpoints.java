package com.vdzon.javalin.server1.rest;

import com.vdzon.javalin.server1.domain.UserRole;
import io.javalin.Javalin;

import static io.javalin.security.SecurityUtil.roles;

public class RestEndpoints {

    public RestEndpoints(Javalin app){
        app.get("/insecure", ctx -> ctx.result("Insecured page"));
        app.get("/secure", ctx -> ctx.result("Secured page"), roles(UserRole.ROLE_ONE));
    }
}

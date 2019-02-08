package com.vdzon.javalin.server1.domain;

import io.javalin.security.Role;

public enum MyRole implements Role {
    NONE, ROLE_ONE, ROLE_TWO, ROLE_THREE;
}
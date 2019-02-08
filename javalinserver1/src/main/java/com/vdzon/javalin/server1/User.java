package com.vdzon.javalin.server1;

public class User {
    private String username;
    private String password;
    private MyRole role;

    public User(String username, String password, MyRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public MyRole getRole() {
        return role;
    }

    public void setRole(MyRole role) {
        this.role = role;
    }
}

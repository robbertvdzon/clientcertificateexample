package com.vdzon.javalin.server1.usermanagement;

import com.vdzon.javalin.server1.domain.MyRole;
import com.vdzon.javalin.server1.domain.User;

import java.util.Arrays;
import java.util.List;

public class UserManagement {
    private List<User> users;

    public UserManagement(){
        users = populateUsers();
    }

    public List<User> getUsers(){
        return users;
    }

    private List<User> populateUsers() {
        return Arrays.asList(
                new User("user","passwd", MyRole.ROLE_ONE),
                new User("user2","passwd", MyRole.ROLE_TWO),
                new User("user3","passwd", MyRole.ROLE_THREE)
        );
    }
}

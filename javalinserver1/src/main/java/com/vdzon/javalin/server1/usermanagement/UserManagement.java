package com.vdzon.javalin.server1.usermanagement;

import com.vdzon.javalin.server1.domain.UserRole;
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
                new User("user","passwd", UserRole.ROLE_ONE),
                new User("user2","passwd", UserRole.ROLE_TWO),
                new User("user3","passwd", UserRole.ROLE_THREE),
                new User("basicauth_client", "passwd", UserRole.ROLE_ONE)
        );
    }
}

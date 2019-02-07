package com.example.server1;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class MyRestController1 {

    /*
     * Does not need any certificate or authentication
     */
    @GetMapping("/insecure")
    public String getInsecure(){
        return "Insecure page";
    }

    /*
     * Needs Basic Authentication (no client cert is required)
     */
    @GetMapping("/insecureAuthenticated")
    @RolesAllowed("BA_USER")
//    @PreAuthorize("hasRole('BA_USER')")
    public String getInsecureAuthenticated(){
        return "Secure authenticated page";
    }

}

package com.example.server2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class MyRestController2 {
    /*
     * Needs client certificate (without any authentication)
     */
    @GetMapping("/secure")
    public String getSecure() {
        return "Secure page";
    }

    /*
     * Needs client certificate, and authentication from the user in the client certificate
     */
    @GetMapping("/secureAuthenticated")
    @RolesAllowed("CERTIFICATE_USER")
    public String getSecureCAAuthenticated() {
        return "Secure authenticated page";
    }


}

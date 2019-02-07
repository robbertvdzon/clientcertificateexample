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
    public String getSecure(){
        return "Secure page";
    }

    /*
     * Needs client certificate, and Basic Authentication
     */
    @GetMapping("/secureBAAuthenticated")
    @RolesAllowed("BA_USER")
//  the following two annotations could also be used!
//    @PreAuthorize("hasAuthority('ROLE_BA_USER')")
//    @PreAuthorize("hasRole('BA_USER')")
    public String getSecureBAAuthenticated(){
//    public String getSecureBAAuthenticated(Authentication authentication){
//        System.out.println("BAauth:"+authentication);
//        System.out.println(authentication.getPrincipal());
//        System.out.println(authentication.getClass());
        return "Secure authenticated page";
    }


    /*
     * Needs client certificate, and authentication from the user in the client certificate
     */
    @GetMapping("/secureCAAuthenticated")
    @RolesAllowed("CERTIFICATE_USER")
//  the following two annotations could also be used!
//    @PreAuthorize("hasAuthority('ROLE_CERTIFICATE_USER')")
//    @PreAuthorize("hasRole('CERTIFICATE_USER')")
    public String getSecureCAAuthenticated(){
        return "Secure authenticated page";
    }


}

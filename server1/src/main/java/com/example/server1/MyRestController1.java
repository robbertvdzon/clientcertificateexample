package com.example.server1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController1 {

    /*
     * Does not need any certificate or authentication
     */
    @GetMapping("/insecure")
    public String getInsecure() {
        return "Insecure page";
    }

    /*
     * Needs Basic Authentication (no client cert is required)
     */
    @GetMapping("/insecureAuthenticated")
    public String getInsecureAuthenticated() {
        return "Secure authenticated page";
    }

}

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController1 {

    @GetMapping("/")
    public String getRoot() {
        return "Hello World";
    }

    @GetMapping("/json")
    public MyJson getJson() {
        return new MyJson("Hello World");
    }

    @PostMapping("/json")
    public String postJson(@RequestBody MyJson myJson) {
        return "Hello : " + myJson.getText();
    }


}

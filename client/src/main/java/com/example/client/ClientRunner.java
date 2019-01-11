package com.example.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClientRunner implements CommandLineRunner {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void run(String...args) throws Exception {
        System.out.println("Start run");
        String url = "https://localhost:8443";
        ResponseEntity exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        System.out.println(exchange.getBody());
    }
}
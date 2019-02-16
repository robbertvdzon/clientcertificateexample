package com.example.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.io.IOException;

@Component
public class ClientRunner implements CommandLineRunner {

    @Autowired()
    @Qualifier("RestTemplateWithCertificate")
    RestTemplate restTemplateWithCertificate;

    @Autowired()
    @Qualifier("RestTemplateWithoutCertificate")
    RestTemplate restTemplateWithoutCertificate;

    @Autowired()
    @Qualifier("RestTemplateWithoutCertificateWithBasicAuth")
    RestTemplate restTemplateWithoutCertificateWithBasicAuth;

    @Autowired()
    @Qualifier("RestTemplateWithCertificateWithBasicAuth")
    RestTemplate restTemplateWithCertificateWithBasicAuth;

    int failureCount = 0;

    @Override
    public void run(String...args) throws IOException {
        while (true) {

            System.out.println("\n\nPress enter to start a new test");
            String line = System.console().readLine();
            if (line.trim().isEmpty()){
                runTests();
            }
            else if (line.trim().equals("1")){
                pageShouldSucceed("/secureCAAuthenticated", restTemplateWithCertificateWithBasicAuth);
            }
            else if (line.trim().equals("2")){
                pageShouldSucceed("/secureBAAuthenticated", restTemplateWithCertificateWithBasicAuth);
            }
            else{
                System.out.println("Unknown command");
            }
        }
//        System.exit(0);
    }

    public void runTests() {
        failureCount = 0;

        System.out.println("\n========================== TESTS WITHOUT CERTIFICATE WITHOUT BASIC AUTH=========================");
        RestTemplate restTemplate = restTemplateWithoutCertificate;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldFail("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureBAAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secureCAAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITHOUT CERTIFICATE WITH BASIC AUTH=========================");
        restTemplate = restTemplateWithoutCertificateWithBasicAuth;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldSucceed("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureBAAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secureCAAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITH CERTIFICATE WITHOUT BASIC AUTH =========================");
        restTemplate = restTemplateWithCertificate;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldFail("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureBAAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secureCAAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITH CERTIFICATE WITH BASIC AUTH =========================");
        restTemplate = restTemplateWithCertificateWithBasicAuth;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldSucceed("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secure", restTemplate);
        pageShouldSucceed("https://localhost:8443/secureBAAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secureCAAuthenticated", restTemplate);

        System.out.println(failureCount==0?"\n\nAll tests passed!":"\n\n"+failureCount+" Tests failed!! <--------");

    }


    private void pageShouldSucceed(String s, RestTemplate restTemplate) {
        tryPage(s, restTemplate, true);
    }

    private void pageShouldFail(String s, RestTemplate restTemplate) {
        tryPage(s, restTemplate, false);
    }

    private void tryPage(String url, RestTemplate restTemplate, boolean shouldSucceed) {
        boolean result;
        int trySize = 45;
        String logString = String.format("%-"+trySize+"."+trySize+"s", url+"  ");

        try {
            ResponseEntity exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            logString+=": "+exchange.getStatusCode().value();//+":"+exchange.getBody();
            result = exchange.getStatusCode().is2xxSuccessful();
        }
        catch (Exception ex){
//            System.out.println(ex.getMessage());
            logString+=": "+ex.getClass().getSimpleName();
            result = false;
        }
        int logStringSize = 60;
        logString = String.format("%-"+logStringSize+"."+logStringSize+"s", logString);
        logString+=result==shouldSucceed ? " : --> OK" : " : --------------------> FAILED!";

        System.out.println(logString);

        if (result!=shouldSucceed) failureCount++;
    }
}
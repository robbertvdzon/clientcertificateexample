package com.example.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ClientRunner implements CommandLineRunner {

    @Autowired()
    @Qualifier("RestTemplateWithCertificateClient1")
    RestTemplate restTemplateWithCertificateClient1;

    @Autowired()
    @Qualifier("RestTemplateWithCertificateClient2")
    RestTemplate restTemplateWithCertificateClient2;

    @Autowired()
    @Qualifier("RestTemplateWithoutCertificate")
    RestTemplate restTemplateWithoutCertificate;

    @Autowired()
    @Qualifier("RestTemplateWithoutCertificateWithBasicAuth")
    RestTemplate restTemplateWithoutCertificateWithBasicAuth;


    int failureCount = 0;

    @Override
    public void run(String... args) throws IOException {
        runTests();
        System.exit(0);
    }

    public void runTests() {
        failureCount = 0;

        System.out.println("\n========================== TESTS WITHOUT CERTIFICATE WITHOUT BASIC AUTH=========================");
        RestTemplate restTemplate = restTemplateWithoutCertificate;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldFail("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITHOUT CERTIFICATE WITH BASIC AUTH=========================");
        restTemplate = restTemplateWithoutCertificateWithBasicAuth;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldSucceed("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldFail("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITH CERTIFICATE, CLIENT 1 =========================");
        restTemplate = restTemplateWithCertificateClient1;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldFail("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secure", restTemplate);
        pageShouldFail("https://localhost:8443/secureAuthenticated", restTemplate);

        System.out.println("\n========================== TESTS WITH CERTIFICATE, CLIENT 2 =========================");
        restTemplate = restTemplateWithCertificateClient2;
        pageShouldSucceed("http://localhost:8091/insecure", restTemplate);
        pageShouldFail("http://localhost:8091/insecureAuthenticated", restTemplate);
        pageShouldSucceed("https://localhost:8443/secure", restTemplate);
        pageShouldSucceed("https://localhost:8443/secureAuthenticated", restTemplate);

        System.out.println(failureCount == 0 ? "\n\nAll tests passed!" : "\n\n" + failureCount + " Tests failed!! <--------");

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
        String logString = String.format("%-" + trySize + "." + trySize + "s", url + "  ");

        try {
            ResponseEntity exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            logString += ": " + exchange.getStatusCode().value();//+":"+exchange.getBody();
            result = exchange.getStatusCode().is2xxSuccessful();
        } catch (Exception ex) {
            logString += ": " + ex.getClass().getSimpleName();
            result = false;
        }
        int logStringSize = 60;
        logString = String.format("%-" + logStringSize + "." + logStringSize + "s", logString);
        logString += result == shouldSucceed ? " : --> OK" : " : --------------------> FAILED!";

        System.out.println(logString);

        if (result != shouldSucceed) failureCount++;
    }
}
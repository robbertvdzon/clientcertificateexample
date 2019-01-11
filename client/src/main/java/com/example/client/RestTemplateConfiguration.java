package com.example.client;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {

        String keystore = "classpath:clientkeystore.jks";
        String keystorePasswd = "passwd";
        String keyPasswd = "passwd";
        String truststore = "classpath:clienttruststore.jks";
        String trustPasswd = "passwd";

        try {
            javax.net.ssl.SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(new URL(keystore),
                            keystorePasswd.toCharArray(),
                            keyPasswd.toCharArray())
                    .loadTrustMaterial(new URL(truststore),
                            trustPasswd.toCharArray())
                    .build();

            HttpClient client = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();

            RestTemplateBuilder restTemplateBuilder = builder
                    .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                    .setReadTimeout(Duration.ofSeconds(60))
                    .setConnectTimeout(Duration.ofSeconds(60));
//            if (basicAuthUsername != null && basicAuthPasswd != null) {
//                restTemplateBuilder = restTemplateBuilder.basicAuthentication(basicAuthUsername, basicAuthPasswd);
//            }
            return restTemplateBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

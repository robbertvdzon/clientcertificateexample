package com.example.client;


import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {

    @Bean ("RestTemplateWithCertificateWithBasicAuth")
    public RestTemplate getRestTemplateWithCertificateAndBasicAuth(RestTemplateBuilder builder) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return getRestTemplate(builder, true, true);
    }

    @Bean ("RestTemplateWithCertificate")
    public RestTemplate getRestTemplateWithCertificate(RestTemplateBuilder builder) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return getRestTemplate(builder, true, false);
    }

    @Bean ("RestTemplateWithoutCertificateWithBasicAuth")
    public RestTemplate getRestTemplateWithoutCertificateWithBasicAuth(RestTemplateBuilder builder) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return getRestTemplate(builder, false, true);
    }

    @Bean ("RestTemplateWithoutCertificate")
    public RestTemplate getRestTemplateWithoutCertificate(RestTemplateBuilder builder) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return getRestTemplate(builder, false, false);
    }

    public RestTemplate getRestTemplate(RestTemplateBuilder builder, boolean clientCert, boolean basicAuth) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        String keystoreLocation = "clientkeystore.p12";
        String keystorePasswd = "passwd";
        String keyPasswd = "passwd";
        String truststoreLocation = "clienttruststore.p12";
        String trustPasswd = "passwd";

        KeyStore truststore = KeyStore.getInstance("PKCS12");
        truststore.load(new ClassPathResource(truststoreLocation).getInputStream(), trustPasswd.toCharArray());

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new ClassPathResource(keystoreLocation).getInputStream(), keystorePasswd.toCharArray());

        try {

            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();

            sslContextBuilder.loadTrustMaterial(truststore, new TrustSelfSignedStrategy());

            if (clientCert) {
                sslContextBuilder.loadKeyMaterial(keystore, keyPasswd.toCharArray());
            }

            SSLContext sslContext = sslContextBuilder.build();

            HttpClient client = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .build();

            RestTemplateBuilder restTemplateBuilder = builder
                    .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                    .setReadTimeout(Duration.ofSeconds(60))
                    .setConnectTimeout(Duration.ofSeconds(60));

            if (basicAuth) {
                restTemplateBuilder = restTemplateBuilder.basicAuthentication("basicauth_client", "passwd");
            }

            return restTemplateBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

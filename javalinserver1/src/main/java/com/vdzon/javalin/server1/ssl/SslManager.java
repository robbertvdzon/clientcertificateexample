package com.vdzon.javalin.server1.ssl;

import com.vdzon.javalin.server1.Application;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SslManager {
    public SslManager(Javalin app){

//        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//                new javax.net.ssl.HostnameVerifier(){
//
//                    public boolean verify(String hostname,
//                                          javax.net.ssl.SSLSession sslSession) {
//                        if (hostname.equals("localhost")) {
//                            return true;
//                        }
//                        return false;
//                    }
//                });

        app.server(() -> {
            org.eclipse.jetty.server.Server server = new Server();
            ServerConnector sslConnector = new ServerConnector(server, getSslContextFactory());
            sslConnector.setPort(443);
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(80);
            server.setConnectors(new Connector[]{sslConnector, connector});
            return server;
        });


    }

    private SslContextFactory getSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(Application.class.getResource("/serverkeystore.p12").toExternalForm());
        sslContextFactory.setKeyStoreType("PKCS12");
        sslContextFactory.setKeyStorePassword("passwd");

        // the following is needed for requiring a client certificate
        sslContextFactory.setTrustStorePath(Application.class.getResource("/servertruststore.p12").toExternalForm());
        sslContextFactory.setTrustStoreType("PKCS12");
        sslContextFactory.setTrustStorePassword("passwd");
        sslContextFactory.setNeedClientAuth(true);
        sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS");


        return sslContextFactory;
    }
}

package com.demohttpswithclient.ssl;

import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class SslManager {
    public void init(Javalin app) {

        app.server(() -> {
            Server server = new Server();
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
        sslContextFactory.setKeyStorePath(SslContextFactory.class.getResource("/serverkeystore.p12").toExternalForm());
        sslContextFactory.setKeyStoreType("PKCS12");
        sslContextFactory.setKeyStorePassword("passwd");

        // the following is needed for requiring a client certificate
        sslContextFactory.setTrustStorePath(SslContextFactory.class.getResource("/servertruststore.p12").toExternalForm());
        sslContextFactory.setTrustStoreType("PKCS12");
        sslContextFactory.setTrustStorePassword("passwd");
        sslContextFactory.setNeedClientAuth(true);
        sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS");


        return sslContextFactory;
    }
}

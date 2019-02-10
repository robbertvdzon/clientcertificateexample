package com.properties;

import io.javalin.Javalin;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PropertiesApplication {
    Javalin app;

    public static void main(String[] args) throws IOException {
        new PropertiesApplication().start();
    }

    public void start() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = PropertiesApplication.class.getResource("/application.yml").openStream();
        ApplicationProperties properties = yaml.load(inputStream);

        app = Javalin.create();
        new RestEndpoints().initRestEndpoints(app, properties);
        app.start(8080);
    }

    public void stop(){
        app.stop();
    }
}

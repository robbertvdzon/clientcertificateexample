package com.httpclient;

import io.javalin.Javalin;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;

import java.net.ConnectException;
import java.time.Duration;

public class RestEndpoints {

    public void initRestEndpoints(Javalin app) {
        app.get("/", ctx -> ctx.result("Hello World from javalin"));
        app.get("/json", ctx -> ctx.json(new MyJson("Hello World")));
        app.post("/json", ctx -> ctx.result("Got post of " + ctx.bodyAsClass(MyJson.class).getText()));
        app.get("/external", ctx -> ctx.result(getFromExternalWithRetryAndCircuitBreaker()));
    }


    private String getFromExternalWithRetryAndCircuitBreaker(){
        RetryPolicy<Object> retryPolicy = new RetryPolicy<>()
                .handle(RuntimeException.class)
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);

        CircuitBreaker<Object> circuitBreaker = new CircuitBreaker<>();

        Fallback<Object> fallback = Fallback.of(this::getFromExternalFallback);

        return Failsafe.with(fallback, retryPolicy, circuitBreaker).get(()->getFromExternal());
    }

    private String getFromExternal() {
        System.out.println("----> get from ex");
        if (true) throw new RuntimeException("sample");
        return "from external";

    }

    private String getFromExternalFallback() {
        System.out.println("----> get from fallback");
        return "from fallback";

    }

}

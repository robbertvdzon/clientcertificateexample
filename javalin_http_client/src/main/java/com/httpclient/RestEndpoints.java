package com.httpclient;

import io.javalin.Javalin;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class RestEndpoints {
    Logger log = LoggerFactory.getLogger(RestEndpoints.class);

    private RetryPolicy<Object> retryPolicy;
    private CircuitBreaker<Object> circuitBreakerPolicy;
    private Fallback<Object> fallbackPolicy;

    // for demo and debugging purposes:
    private int delay = 0;


    public void initRestEndpoints(Javalin app) {
        buildPolicies();
        app.get("/", ctx -> ctx.result("Hello World from javalin"));
        app.get("/json", ctx -> ctx.json(new MyJson("Hello World")));
        app.post("/json", ctx -> ctx.result("Got post of " + ctx.bodyAsClass(MyJson.class).getText()));
        app.get("/external", ctx -> ctx.result(getFromExternalWithRetryAndCircuitBreaker()));
        app.get("/delayedresponse", ctx -> ctx.result(getExternal()));
        app.get("/disabledelay", ctx -> ctx.result(setDelay(0)));
        app.get("/enabledelay", ctx -> ctx.result(setDelay(1000)));
    }

    private String setDelay(int i) {
        delay = i;
        log.info("----> set delay to "+i);
        return "ok";
    }

    private String getExternal() {
        sleep(delay);
        return "from external server";
    }

    private void buildPolicies() {
        retryPolicy = new RetryPolicy<>()
                .handle(IOException.class)
                .withDelay(Duration.ofMillis(1)) // only this small for demo purposes!
                .withMaxRetries(2)
                .onRetry((ctx)->log.info("  ----->Retry"))
                .onRetriesExceeded((ctx)->log.info("  ----->Retries exceeded, use fallback"));


        circuitBreakerPolicy = new CircuitBreaker<>()
                .handle(IOException.class)
                .withFailureThreshold(10, 12)
                .withSuccessThreshold(3, 5)
                .withDelay(Duration.ofSeconds(1)) // only this small for demo purposes!
                .onClose(() -> log.info("=======> Circuit Closed"))
                .onOpen(() -> log.warn("=======> Circuit Opened, use fallback for all the call's in the next second"))
                .onHalfOpen(() -> log.info("\"=======> Circuit Half-Open, try the endpoint again"));

        fallbackPolicy = Fallback.of(this::getFromExternalFallback);
    }

    private String getFromExternalWithRetryAndCircuitBreaker() {
        return Failsafe.with(fallbackPolicy, retryPolicy, circuitBreakerPolicy).get(this::getFromExternal);
    }

    private String getFromExternal() throws IOException {
        log.info("----> try to call external system");
        String result = Request
                .Get("http://localhost:8080/delayedresponse")
                .connectTimeout(10)
                .socketTimeout(10)
                .execute()
                .returnContent()
                .asString();
        log.info("----> call to external succeeded");
        return result;
    }

    private String getFromExternalFallback() {
        log.info("----> return from fallback");
        return "from fallback";
    }

    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

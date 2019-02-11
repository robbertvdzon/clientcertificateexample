package com.httpclient;

import io.javalin.Javalin;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.time.Duration;

public class RestEndpoints {
    private RetryPolicy<Object> retryPolicy;
    private CircuitBreaker<Object> circuitBreakerPolicy;
    private Fallback<Object> fallbackPolicy;


    public void initRestEndpoints(Javalin app) {
        buildPolicies();
        app.get("/", ctx -> ctx.result("Hello World from javalin"));
        app.get("/json", ctx -> ctx.json(new MyJson("Hello World")));
        app.post("/json", ctx -> ctx.result("Got post of " + ctx.bodyAsClass(MyJson.class).getText()));
        app.get("/external", ctx -> ctx.result(getFromExternalWithRetryAndCircuitBreaker()));
        app.get("/delayedresponse", ctx -> ctx.result(getDelayedResult()));
        app.get("/disabledelay", ctx -> ctx.result(setDelay(0)));
        app.get("/enabledelay", ctx -> ctx.result(setDelay(1000)));
    }

    private int delay = 0;

    private String setDelay(int i) {
        delay = i;
        return "ok";
    }

    private String getDelayedResult() {
        System.out.println("----> get delayed");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "from slow server";
    }

    private void buildPolicies() {
        retryPolicy = new RetryPolicy<>()
                .handle(IOException.class)
//                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);

        circuitBreakerPolicy = new CircuitBreaker<>()
                .handle(IOException.class)
//                .handleIf((Object exchange, Throwable ex) -> {
//                    boolean badRequest = exchange != null && StatusCodes.BAD_REQUEST == exchange.getStatusCode();
//                    return badRequest || ex != null;
//                    return false;
//                })
                // If 7 out of 10 requests fail Open the circuit
                .withFailureThreshold(7, 10)
                // When half open if 3 out of 5 requests succeed close the circuit
                .withSuccessThreshold(3, 5)
                // Delay this long before half opening the circuit
                .withDelay(Duration.ofSeconds(1))
                .onClose(() ->
                        System.out.println("XXXCircuit Closed"))
                .onOpen(() ->
                        System.out.println("XXXCircuit Opened"))
                .onHalfOpen(() ->
                        System.out.println("XXXCircuit Half-Open"));

        fallbackPolicy = Fallback.of(this::getFromExternalFallback);
    }

    private String getFromExternalWithRetryAndCircuitBreaker() {
        return Failsafe.with(fallbackPolicy, retryPolicy, circuitBreakerPolicy).get(this::getFromExternal);
//        return Failsafe.with(fallbackPolicy, circuitBreakerPolicy).get(this::getFromExternal);
    }

    private String getFromExternal() throws IOException {
//        if (true) throw new SocketTimeoutException();
        System.out.println("----> get from ex");
        return Request
                .Get("http://localhost:8080/delayedresponse")
                .connectTimeout(10)
                .socketTimeout(10)
                .execute()
                .returnContent()
                .asString();
    }

private int c = 0;

    private String getFromExternalFallback() {
        System.out.println("----> get from fallback:"+c++);
        return "from fallback";
    }

}

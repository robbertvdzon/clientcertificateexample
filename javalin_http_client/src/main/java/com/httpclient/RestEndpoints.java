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
    }

    private void buildPolicies() {
        retryPolicy = new RetryPolicy<>()
                .handle(RuntimeException.class)
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3);

        circuitBreakerPolicy = new CircuitBreaker<>()
                .handleIf((Object exchange, Throwable ex) -> {
//                    boolean badRequest = exchange != null && StatusCodes.BAD_REQUEST == exchange.getStatusCode();
//                    return badRequest || ex != null;
                    return false;
                })
                // If 7 out of 10 requests fail Open the circuit
                .withFailureThreshold(7, 10)
                // When half open if 3 out of 5 requests succeed close the circuit
                .withSuccessThreshold(3, 5)
                // Delay this long before half opening the circuit
                .withDelay(Duration.ofSeconds(2))
                .onClose(() -> System.out.println("Circuit Closed"))
                .onOpen(() -> System.out.println("Circuit Opened"))
                .onHalfOpen(() -> System.out.println("Circuit Half-Open"));

        fallbackPolicy = Fallback.of(this::getFromExternalFallback);
    }

    private String getFromExternalWithRetryAndCircuitBreaker() {
        return Failsafe.with(fallbackPolicy, retryPolicy, circuitBreakerPolicy).get(this::getFromExternal);
    }

    private String getFromExternal() {
        try {
            System.out.println("----> get from ex");
            return Request
                    .Get("http://localhost:8080/")
                    .execute()
                    .returnContent()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private String getFromExternalFallback() {
        System.out.println("----> get from fallback");
        return "from fallback";
    }

}

package com.samuelji.fishgame.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DynamicRateLimiterTest {

    @LocalServerPort
    private int port;

    @Test
    public void testConcurrentRequestsAreRateLimited() throws Exception {
        // Define test parameters
        final String testUrl = "http://localhost:" + port + "/user/generate-token";
        final int numConcurrentRequests = 1200; // Higher than our limit

        // Counters for response codes
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger rateLimitedCount = new AtomicInteger(0);

        // Create thread coordination objects
        ExecutorService executor = Executors.newFixedThreadPool(numConcurrentRequests);
        CountDownLatch readyLatch = new CountDownLatch(numConcurrentRequests);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(numConcurrentRequests);

        // Create and submit all threads
        for (int i = 0; i < numConcurrentRequests; i++) {
            executor.submit(() -> {
                try {
                    // Signal ready and wait for start signal
                    readyLatch.countDown();
                    startLatch.await();

                    // Make HTTP request
                    URI uri = URI.create(testUrl);
                    URL url = uri.toURL();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();

                    // Count the response types
                    if (responseCode == 200) {
                        successCount.incrementAndGet();
                    } else if (responseCode == 429) {
                        rateLimitedCount.incrementAndGet();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    completeLatch.countDown();
                }
            });
        }

        // Coordinate the test execution
        readyLatch.await();
        startLatch.countDown();
        completeLatch.await();

        executor.shutdown();

        // Verify that rate limiting was applied
        System.out.println("Successful requests: " + successCount.get());
        System.out.println("Rate limited requests: " + rateLimitedCount.get());

        // Assert that some requests were rate limited
        assertTrue(rateLimitedCount.get() > 0, "No requests were rate limited");
        // Since the normal request limit is 1000, we can expect to see around that many
        // successful requests
        // This assertion might need adjustment depending on server behavior
        assertTrue(successCount.get() < numConcurrentRequests, "All requests were successful despite rate limiting");
    }
}

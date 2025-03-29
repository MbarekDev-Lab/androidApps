package com.plracticalcoding.testCode.autoLogout;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;

public class LoginApp {
    // Semaphore with 3 permits - only 3 login attempts can occur at the same time
    private static final Semaphore semaphore = new Semaphore(3);

    // Store user credentials in-memory for this example (replace with actual DB in production)
    private static final Map<String, String> userDatabase = new HashMap<>();

    // Track failed login attempts per user (for rate limiting)
    private static final Map<String, AtomicInteger> failedAttempts = new HashMap<>();

    // Max allowed failed attempts before blocking login attempts for the user
    private static final int MAX_FAILED_ATTEMPTS = 3;

    public static void main(String[] args) {
        // Dummy users: Username -> Password
        userDatabase.put("user1", "password123");
        userDatabase.put("user2", "password456");
        userDatabase.put("user3", "password789");

        // Executor service to manage threads more efficiently
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Simulate multiple login requests
        for (int i = 1; i <= 10; i++) {
            final String username = "user" + i;
            int finalI = i;
            executor.submit(() -> handleLogin(username, "password" + finalI));  // password is wrong for some users
        }

        // Shutdown executor after all tasks are finished
        executor.shutdown();
    }

    private static void handleLogin(String username, String password) {
        try {
            // First, check if the user has failed too many attempts
            if (Objects.requireNonNull(failedAttempts.getOrDefault(username, new AtomicInteger(0))).get() >= MAX_FAILED_ATTEMPTS) {
                System.out.println("User " + username + " is blocked due to too many failed login attempts.");
                return;
            }

            // Try to acquire a permit before processing the login
            System.out.println("User " + username + " is trying to log in...");
            semaphore.acquire();

            // Simulate credential validation (replace this with actual DB logic in a real app)
            boolean loginSuccess = validateCredentials(username, password);

            if (loginSuccess) {
                System.out.println("User " + username + " logged in successfully!");
                // Reset failed attempts on successful login
                failedAttempts.put(username, new AtomicInteger(0));
            } else {
                System.out.println("User " + username + " failed to log in.");
                incrementFailedAttempts(username);
            }

            // Release the permit after processing the login attempt
            semaphore.release();
        } catch (InterruptedException e) {
            System.err.println("Error while handling login for user " + username + ": " + e.getMessage());
        }
    }

    private static boolean validateCredentials(String username, String password) {
        // Simulating credential validation (replace with actual DB or authentication service)
        String storedPassword = userDatabase.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    private static void incrementFailedAttempts(String username) {
        // Increment failed login attempts for the user
        failedAttempts.putIfAbsent(username, new AtomicInteger(0));
        failedAttempts.get(username).incrementAndGet();

        // Optionally: Block the user after a certain number of failed attempts
        if (failedAttempts.get(username).get() >= MAX_FAILED_ATTEMPTS) {
            System.out.println("User " + username + " is blocked for too many failed attempts.");
        }
    }
}


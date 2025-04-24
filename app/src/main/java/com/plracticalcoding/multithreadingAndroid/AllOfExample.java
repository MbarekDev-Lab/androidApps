package com.plracticalcoding.multithreadingAndroid;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AllOfExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            // Simulate some work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return "Result from Task 1";
        });

        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            // Simulate some work
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            return 42;
        });

        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> {
            // Simulate some work (no return value)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.out.println("Task 3 completed");
        });

        CompletableFuture<Void> async = CompletableFuture.allOf(task1, task2, task3);

        // Wait for all tasks to complete (blocking for demonstration)
        async.get();

        // Now all tasks are finished, we can access results
        // Note: We access results from the *original* CompletableFutures, not 'async'
        String result1 = task1.get();
        Integer result2 = task2.get();

        System.out.println("All tasks completed.");
        System.out.println("Result 1: " + result1);
        System.out.println("Result 2: " + result2);
        // Note: Task 3 doesn't have a result, but its side effect (print) has happened.
    }
}
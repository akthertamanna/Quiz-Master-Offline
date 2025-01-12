package com.skappsstore.quizgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.splashscreen.SplashScreen;
import com.skappsstore.quizgame.activity.MainActivity;
import com.skappsstore.quizgame.database.QuizDataHandler;
import com.skappsstore.quizgame.utilities.Helper;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SplashClass extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        // Set the splash screen condition
        splashScreen.setKeepOnScreenCondition(() -> false);
        fetchQuizzes();
    }


    private void fetchQuizzes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // API level 24 or higher: Use CompletableFuture
            fetchQuizzesWithCompletableFuture();
        } else {
            // API level below 24: Use ExecutorService and FutureTask
            fetchQuizzesWithExecutorService();
        }
    }

    // Implementation for API level 24 and above
    private void fetchQuizzesWithCompletableFuture() {
        // Create an ExecutorService for background tasks
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Use CompletableFuture for task management
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompletableFuture.runAsync(() -> QuizDataHandler.fetchCategories(Helper.quizDataUrl, getApplicationContext()), executor).thenRun(() -> {
                // Proceed to the main activity on the UI thread
                runOnUiThread(this::proceedToMainActivity);
            });
        }

        // Run fetchAndCacheQuizzes concurrently
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompletableFuture.runAsync(() -> QuizDataHandler.fetchAndCacheQuizzes(Helper.quizDataUrl, getApplicationContext()), executor);
        }

        // Shut down the executor
        executor.shutdown();
    }

    // Implementation for API levels below 24
    private void fetchQuizzesWithExecutorService() {
        // Create an ExecutorService for background tasks
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Create a FutureTask for fetchCategories
        Future<?> fetchCategoriesTask = executor.submit(() -> QuizDataHandler.fetchCategories(Helper.quizDataUrl, getApplicationContext()));

        // Submit fetchAndCacheQuizzes as an independent task
        executor.submit(() -> QuizDataHandler.fetchAndCacheQuizzes(Helper.quizDataUrl, getApplicationContext()));

        // Run a task to wait for fetchCategoriesTask to complete
        executor.submit(() -> {
            try {
                // Wait for fetchCategories to complete
                fetchCategoriesTask.get(); // This blocks until the task is finished
                // Once completed, switch to the UI thread to proceed to the main activity
                runOnUiThread(this::proceedToMainActivity);
            } catch (Exception ignored) {
            }
        });

        // Shut down the executor
        executor.shutdown();
    }

    private void proceedToMainActivity() {
        Intent mainIntent = new Intent(SplashClass.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
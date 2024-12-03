package com.plracticalcoding.asyncTaskClass;// MainActivity.java

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.plracticalcoding.myapplication.R;

public class MainActivity22 extends AppCompatActivity {

    private TextView textViewResult;
    private ProgressBar progressBar;
    private Button buttonStartTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main2);

        textViewResult = findViewById(R.id.textViewResult22);
        progressBar = findViewById(R.id.progressBar22);
        buttonStartTask = findViewById(R.id.buttonStartTask22);

        buttonStartTask.setOnClickListener(v -> {
            // Start the AsyncTask
            new MyAsyncTask().execute(10);  // Pass a parameter to the task
        });
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        // Runs on the UI thread before the background task starts
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewResult.setText("Task Starting...");
            progressBar.setVisibility(View.VISIBLE);
        }

        // Runs in the background (off the UI thread)
        @Override
        protected String doInBackground(Integer... params) {
            int count = params[0];  // Get the number passed from execute()
            for (int i = 1; i <= count; i++) {
                try {
                    // Simulate a long-running operation
                    Thread.sleep(1000);  // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Update progress
                publishProgress((i * 100) / count);  // Calculate progress percentage
            }
            return "Task Completed!";
        }

        // Runs on the UI thread when publishProgress() is called
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress bar
            progressBar.setProgress(progress[0]);
            textViewResult.setText("Progress: " + progress[0] + "%");
        }

        // Runs on the UI thread after the background task is finished
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textViewResult.setText(result);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

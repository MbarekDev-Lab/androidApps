package com.plracticalcoding.multithreadingAndroid.databaseWorkManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.plracticalcoding.db.AppDatabase;
import com.plracticalcoding.db.MyDao;
import com.plracticalcoding.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resultTextView = findViewById(R.id.result_text_view);

        //Build database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database").build();
        MyDao myDao = db.myDao();

        // Add some data to the database (for testing purposes):
        new Thread(() -> {
//            myDao.insert(new MyEntity("Data 1"));
//            myDao.insert(new MyEntity("Data 2"));
//            myDao.insert(new MyEntity("Data 3"));
            Log.d(TAG, "Data inserted in db");
        }).start();
        db.close();

        // Create the work request:
        OneTimeWorkRequest fetchDataRequest = new OneTimeWorkRequest.Builder(FetchDataWorker.class).build();

        // Enqueue the work:
        WorkManager.getInstance(this).enqueue(fetchDataRequest);

        // Observe the work status:
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(fetchDataRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            // Get the output data:
                            Data outputData = workInfo.getOutputData();
                            String dbData = outputData.getString("db_data");

                            // Update the UI with the output data:
                            if (dbData != null) {
                                resultTextView.setText(dbData);
                                Log.d(TAG, "Data in MainActivity");
                                Log.d(TAG, dbData);
                            }
                        } else if(workInfo != null && workInfo.getState() == WorkInfo.State.FAILED){
                          //Handle failure
                          Log.e(TAG, "Work Failed in MainActivity");
                        }
                    }
                });
    }
}
package com.plracticalcoding.testCode;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivitytoshowPopRecycleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activitytoshow_pop_recycle_view);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Button showDialogButton = findViewById(R.id.showDialogButton);
        showDialogButton.setOnClickListener(v -> {
            // Retrieve the data from the database
            List<YourDataModel> emailList = getEmailsFromDatabase();
            // Replace this with your actual data retrieval method
            EmailDialogFragment dialogFragment = new EmailDialogFragment(emailList);
            dialogFragment.show(getSupportFragmentManager(), "EmailDialogFragment");
        });

//        Button showDialogButtonxc = findViewById(R.id.showDialogButton);
//        showDialogButton.setOnClickListener(v -> {
//            // Retrieve the data from the database
//            List<YourDataModel> emailList = getEmailsFromDatabase();
//
//            // Create the DialogFragment and pass the data list to it
//            EmailDialogFragment dialogFragment = new EmailDialogFragment(emailList);
//            dialogFragment.show(getSupportFragmentManager(), "EmailDialogFragment");
//        });

    }


    public List<YourDataModel> getEmailsFromDatabase() {
        List<YourDataModel> dataList = new ArrayList<>();

        // Example data retrieval (replace this with actual SQL query)
        // For example, suppose you have retrieved these five columns from your database:
        String column1Value = "John Doe";
        String column2Value = "john@example.com";
        String column3Value = "Customer";
        String column4Value = "Active";
        String column5Value = "2024-10-09";

        // Add each row of data as a new YourDataModel object

        for (int i = 0; i < 5; i++)
            dataList.add(new YourDataModel(column1Value, column2Value, column3Value, column4Value, column5Value));

        // Repeat for more rows of data (you'd loop through your database results)
        // Example:
        // while(resultSet.next()) {
        //     String column1 = resultSet.getString("column1");
        //     String column2 = resultSet.getString("column2");
        //     String column3 = resultSet.getString("column3");
        //     String column4 = resultSet.getString("column4");
        //     String column5 = resultSet.getString("column5");
        //     dataList.add(new YourDataModel(column1, column2, column3, column4, column5));
        // }

        return dataList;
    }

}
package com.example.todolist;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText item;
    Button add;
    ListView listView;
    ArrayList<String> itemsList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.button2);
        item = findViewById(R.id.editTextText2);
        listView = findViewById(R.id.list_item);


        itemsList = FileHelper.readData(this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsList);
        listView.setAdapter(arrayAdapter);

        add.setOnClickListener(v -> {
            String itemText = item.getText().toString();
            itemsList.add(itemText);
            item.setText("");
            FileHelper.writeData(itemsList, getApplicationContext());
            arrayAdapter.notifyDataSetChanged();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Delete");
            alert.setMessage("Do you want to delete this item from the list?");
            alert.setCancelable(false);
            alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            alert.setPositiveButton("Yes", (dialog, which) -> {
                itemsList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                FileHelper.writeData(itemsList, getApplicationContext());
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();

        });
    }
}

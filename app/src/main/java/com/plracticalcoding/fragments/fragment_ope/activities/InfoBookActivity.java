package com.plracticalcoding.fragments.fragment_ope.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.fragments.fragment_ope.adapter.InfoBookAdapter;
import com.plracticalcoding.fragments.model.ModelClass;
import com.plracticalcoding.myapplication.R;

public class InfoBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InfoBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_book);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ModelClass[] modelClasses = new ModelClass[]{
                new ModelClass(R.drawable.first, "The First Book"),
                new ModelClass(R.drawable.first, "The Second Book"),
                new ModelClass(R.drawable.first, "The Third Book"),
                new ModelClass(R.drawable.first, "The Fourth Book"),
                new ModelClass(R.drawable.first, "The Fifth Book")
        };

        adapter = new InfoBookAdapter(modelClasses, this);
        recyclerView.setAdapter(adapter);
    }
}

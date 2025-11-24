package com.plracticalcoding.restAPIRetrofit;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.restAPIRetrofit.adapter.RecycleAdapter;
import com.plracticalcoding.restAPIRetrofit.data.model.ModelClass;
import com.plracticalcoding.restAPIRetrofit.data.storage.RetrofitApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRestApiActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ModelClass> data;
    RecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rest_api);

        recyclerView = findViewById(R.id.mainRestApiActivityrw);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<ModelClass>> call = retrofitApi.getModelClass();

        call.enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelClass>> call, @NonNull Response<List<ModelClass>> response) {
                if (response.isSuccessful()) {
                    data = response.body();
                    recycleAdapter = new RecycleAdapter(data);
                    recyclerView.setAdapter(recycleAdapter);
                } else {
                    Toast.makeText(MainRestApiActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelClass>> call, @NonNull Throwable t) {
                Toast.makeText(MainRestApiActivity.this, "request is failed: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

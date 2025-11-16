package com.plracticalcoding.fragments.fragment_ope;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plracticalcoding.myapplication.R;

public class OpeFragmentActivity extends AppCompatActivity {

    Button replace_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ope_fragment);
        replace_fragment = findViewById(R.id.replace_fragment);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        OpeFirstFragment opeFirstFragment = new OpeFirstFragment();
        fragmentTransaction.add(R.id.ope_fragment, opeFirstFragment);
        fragmentTransaction.commit();

        replace_fragment.setOnClickListener(v -> {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            OpeSecondFragment opeSecondFragment = new OpeSecondFragment();
            fragmentTransaction1.replace(R.id.ope_fragment, opeSecondFragment);
            fragmentTransaction1.commit();
        });


    }
}
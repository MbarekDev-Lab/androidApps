package com.plracticalcoding.fragments.fragment_ope.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plracticalcoding.fragments.fragment_ope.fragments.OpeSecondFragment;
import com.plracticalcoding.myapplication.R;

public class SecontFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secont_fragment);
        Intent intent = getIntent();
        int position = intent.getIntExtra("positions", 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Use the factory method to create the fragment with arguments
        OpeSecondFragment opeSecondFragment = OpeSecondFragment.newInstance(position);

        fragmentTransaction.add(R.id.ope_fragment, opeSecondFragment);
        fragmentTransaction.commit();
    }
}

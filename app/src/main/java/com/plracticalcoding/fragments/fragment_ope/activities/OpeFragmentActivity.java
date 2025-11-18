package com.plracticalcoding.fragments.fragment_ope.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plracticalcoding.fragments.fragment_ope.fragments.ListFragment;
import com.plracticalcoding.fragments.fragment_ope.fragments.OpeSecondFragment;
import com.plracticalcoding.myapplication.R;

public class OpeFragmentActivity extends AppCompatActivity {

    Button replace_fragment;
    Button dialogfragment;
    Button infoBookActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ope_fragment);
        replace_fragment = findViewById(R.id.replace_fragment);
        dialogfragment = findViewById(R.id.dialogfragment);
        infoBookActivity = findViewById(R.id.infoBookActivity);



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListFragment listFragment = new ListFragment();
        fragmentTransaction.add(R.id.ope_fragment, listFragment);
        fragmentTransaction.commit();

        replace_fragment.setOnClickListener(v -> {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();

            // Use the factory method to create the fragment with a default position
            OpeSecondFragment opeSecondFragment = OpeSecondFragment.newInstance(0);

            fragmentTransaction1.replace(R.id.ope_fragment, opeSecondFragment);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();
        });

        dialogfragment.setOnClickListener(v -> {
            startActivity(new Intent(this, DialogFragmentActivity.class));
        });
        infoBookActivity.setOnClickListener(v -> {
            startActivity(new Intent(this, InfoBookActivity.class));
        });

    }
}
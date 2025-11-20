package com.plracticalcoding.fragments.fragment_ope.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.plracticalcoding.fragments.fragment_ope.adapter.ViewPagerApadpter;
import com.plracticalcoding.myapplication.R;

public class CountriesActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ViewPagerApadpter viewPagerApadpter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contries);

        viewPager2 = findViewById(R.id.viewpagerunitedkingdom);
        tabLayout = findViewById(R.id.tablayoutunitedkingdom);

        ViewPagerApadpter viewPageAdapter = new ViewPagerApadpter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(viewPageAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("United Kingdom");
                        break;
                    case 1:
                        tab.setText("France ");
                        break;

                }
            }
        });
        tabLayoutMediator.attach();
    }
}

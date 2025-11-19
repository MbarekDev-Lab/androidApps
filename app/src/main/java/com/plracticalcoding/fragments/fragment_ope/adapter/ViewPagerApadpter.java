package com.plracticalcoding.fragments.fragment_ope.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.plracticalcoding.fragments.fragment_ope.fragments.Fragmentunitedkingdom;

public class ViewPagerApadpter extends FragmentStateAdapter {
    public ViewPagerApadpter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragmentunitedkingdom.newInstace();
                break;
            case 1:
             //   fragment = new SecondFragment();
                break;
            default:
                return null;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

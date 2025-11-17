package com.plracticalcoding.fragments.fragment_ope.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plracticalcoding.myapplication.R;

public class OpeFirstFragment extends Fragment {
    public OpeFirstFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_ope_first, container, false);
    }
}
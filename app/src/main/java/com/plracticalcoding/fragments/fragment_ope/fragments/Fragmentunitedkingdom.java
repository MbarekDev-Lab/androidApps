package com.plracticalcoding.fragments.fragment_ope.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plracticalcoding.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class Fragmentunitedkingdom extends Fragment {

    public static Fragmentunitedkingdom newInstace() {
        return new Fragmentunitedkingdom();
    }

    private ImageView imageViewUnitedKingdom;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmented_kingdom, container, false);
        imageViewUnitedKingdom = view.findViewById(R.id.unitedkindomimgviw);
        progressBar = view.findViewById(R.id.progressBar);

        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Flag_of_the_United_Kingdom_%281-2%29.svg/1280px-Flag_of_the_United_Kingdom_%281-2%29.svg.png").into(imageViewUnitedKingdom, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }


}

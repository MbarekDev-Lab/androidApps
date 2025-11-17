package com.plracticalcoding.fragments.fragment_ope.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plracticalcoding.myapplication.R;

public class OpeSecondFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    public OpeSecondFragment() {
        // Required empty public constructor
    }

    public static OpeSecondFragment newInstance(int position) {
        OpeSecondFragment fragment = new OpeSecondFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ope_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.imageView4);

        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_POSITION, -1);
            int imageResource = -1;

            switch (position) {
                case 0:
                    imageResource = R.drawable.first;
                    break;
                case 1:
                    imageResource = R.drawable.loginbackground;
                    break;
                case 2:
                    imageResource = R.drawable.quiz_pic;
                    break;
                case 3:
                    imageResource = R.drawable.now_number;
                    break;
            }

            if (imageResource != -1) {
                imageView.setImageResource(imageResource);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}

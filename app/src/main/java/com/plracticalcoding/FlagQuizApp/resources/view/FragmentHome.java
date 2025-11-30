package com.plracticalcoding.FlagQuizApp.resources.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plracticalcoding.FlagQuizApp.resources.datadase.DatabaseCopyHelper;
import com.plracticalcoding.myapplication.databinding.FragmentHomeBinding;

import java.io.IOException;

public class FragmentHome extends Fragment {

    FragmentHomeBinding fragmentHomeBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);

        createAndOpenDatabase();

        fragmentHomeBinding.buttonStart.setOnClickListener(v -> {
            //navigation
            /*
            NavDirections directions = FragmentHomeDirections.actionFragmentHomeToFragmentQuiz();
            NavHostFragment.findNavController(this).navigate(directions);

             */

           // Navigation.findNavController(v).navigate(R.id.action_fragmentHome_to_fragmentQuiz);

        });


        return fragmentHomeBinding.getRoot();
    }

    public void createAndOpenDatabase(){

        try(DatabaseCopyHelper helper = new DatabaseCopyHelper(requireActivity())) {

            helper.createDataBase();
            helper.openDataBase();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
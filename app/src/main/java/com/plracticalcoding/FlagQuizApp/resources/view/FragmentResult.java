package com.plracticalcoding.FlagQuizApp.resources.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.FragmentResultBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class FragmentResult extends Fragment {

    FragmentResultBinding fragmentResultBinding;
    float correctNumber = 0F;
    float emptyNumber = 0F;
    float wrongNumber = 0F;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentResultBinding = FragmentResultBinding.inflate(inflater, container, false);

        //int correct = FragmentResultArgs.fromBundle(getArguments()).getCorrect();

        if (getArguments() != null) {
            correctNumber = (float) getArguments().getInt("correct");
            emptyNumber = (float) getArguments().getInt("empty");
            wrongNumber = (float) getArguments().getInt("wrong");
        }

        ArrayList<BarEntry> barEntriesArrayListCorrect = new ArrayList<>();
        ArrayList<BarEntry> barEntriesArrayListEmpty = new ArrayList<>();
        ArrayList<BarEntry> barEntriesArrayListWrong = new ArrayList<>();

        barEntriesArrayListCorrect.add(new BarEntry(0F, correctNumber));
        barEntriesArrayListEmpty.add(new BarEntry(1F, emptyNumber));
        barEntriesArrayListWrong.add(new BarEntry(2F, wrongNumber));

        BarDataSet barDataSetCorrect = createBarDataSet(barEntriesArrayListCorrect, "Correct Number", Color.GREEN, 24F, Color.BLACK);
        BarDataSet barDataSetEmpty = createBarDataSet(barEntriesArrayListEmpty, "Empty Number", Color.BLUE, 24F, Color.BLACK);
        BarDataSet barDataSetWrong = createBarDataSet(barEntriesArrayListWrong, "Wrong Number", Color.RED, 24F, Color.BLACK);

        BarData barData = new BarData(barDataSetCorrect, barDataSetEmpty, barDataSetWrong);
        fragmentResultBinding.resultChart.setData(barData);


        fragmentResultBinding.buttonNewQuiz.setOnClickListener(v -> {

               Navigation.findNavController(v).popBackStack(R.id.fragmentHome,false);

        });

        fragmentResultBinding.buttonExit.setOnClickListener(v -> {

            requireActivity().finish();

        });

        return fragmentResultBinding.getRoot();
    }

    public BarDataSet createBarDataSet(ArrayList<BarEntry> entries, String label, int barColor, float valueTextSize, int valueTextColor) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(barColor);
        dataSet.setValueTextSize(valueTextSize);
        dataSet.setValueTextColor(valueTextColor);
        return dataSet;
    }

}
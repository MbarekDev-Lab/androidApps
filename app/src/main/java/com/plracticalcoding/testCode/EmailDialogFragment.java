package com.plracticalcoding.testCode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmailDialogFragment extends DialogFragment {
    private final List<YourDataModel> emailList;

    public EmailDialogFragment(List<YourDataModel> emailList) {
        this.emailList = emailList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recyclerview, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter adapter = new MyAdapter(emailList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

package com.plracticalcoding.fragments.fragment_ope.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.plracticalcoding.myapplication.R;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private EditText nameEditText;

    public DialogFragment() {

    }

    public static DialogFragment newInstance(String title) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.nameEditText);
        Button okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString("title", getString(R.string.dialog_enter_name));
        getDialog().setTitle(title);

        nameEditText.requestFocus();

        okButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            if (!name.isEmpty()) {
                // You can define an interface to pass data back to the calling fragment/activity
                Toast.makeText(getContext(), getString(R.string.hello_user, name), Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                nameEditText.setError(getString(R.string.error_enter_name));
            }
        });

        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
}

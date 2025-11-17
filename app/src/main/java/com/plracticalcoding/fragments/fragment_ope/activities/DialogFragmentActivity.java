package com.plracticalcoding.fragments.fragment_ope.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.plracticalcoding.fragments.fragment_ope.fragments.MDialogFragment;
import com.plracticalcoding.myapplication.R;

public class DialogFragmentActivity extends AppCompatActivity {

    Button button10;
    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);

        button10 = findViewById(R.id.button10);
        textView5 = findViewById(R.id.textView5);

        button10.setOnClickListener(v -> {
            MDialogFragment fragment = new MDialogFragment();
            fragment.show(getSupportFragmentManager(), "MDialogFragment");
        });


    }
}
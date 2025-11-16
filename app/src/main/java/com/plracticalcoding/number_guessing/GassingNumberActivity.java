package com.plracticalcoding.number_guessing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.plracticalcoding.myapplication.R;

public class GassingNumberActivity extends AppCompatActivity {
    private Button startButton;
    private RadioGroup digitRadioGroup;
    private RadioButton twoDigitsRadioButton, threeDigitsRadioButton, fourDigitsRadioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gassing_number_activity);

        startButton = findViewById(R.id.startButton);
        digitRadioGroup = findViewById(R.id.digitRadioGroup);
        twoDigitsRadioButton = findViewById(R.id.twoDigitsRadioButton);
        threeDigitsRadioButton = findViewById(R.id.threeDigitsRadioButton);
        fourDigitsRadioButton = findViewById(R.id.fourDigitsRadioButton);



        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GassingNumberGameActivity.class);

            if (!twoDigitsRadioButton.isChecked() && !threeDigitsRadioButton.isChecked() && !fourDigitsRadioButton.isChecked()) {
                Snackbar.make(v, "please select a number of digits the numberr of digits that i keep in mind", Snackbar.LENGTH_SHORT).show();
            } else {
                if (twoDigitsRadioButton.isChecked()) {
                    intent.putExtra("twoDigits", true);
                }
                if (threeDigitsRadioButton.isChecked()) {
                    intent.putExtra("threeDigits", true);
                }
                if (fourDigitsRadioButton.isChecked()) {
                    intent.putExtra("fourDigits", true);
                }
                startActivity(intent);
            }

        });
    }

}

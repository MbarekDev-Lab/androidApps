package com.plracticalcoding.quizGame.number_guessing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.plracticalcoding.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class GassingNumberGameActivity extends AppCompatActivity {

    private TextView textViewLast, textViewRight, hintTextView;
    private Button confirmButton;
    private EditText guessEditText;

    private boolean twoDigits, threeDigits, fourDigits;
    private int randomNumber;
    private int guessesLeft = 10;
    private ArrayList<Integer> guessesList = new ArrayList<>();
    private int userAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gassing_number_game);

        confirmButton = findViewById(R.id.confirmButton);
        guessEditText = findViewById(R.id.guessEditText);
        textViewLast = findViewById(R.id.titleTextView);
        textViewRight = findViewById(R.id.guessesLeftTextView);
        hintTextView = findViewById(R.id.hintTextView);

        twoDigits = getIntent().getBooleanExtra("twoDigits", false);
        threeDigits = getIntent().getBooleanExtra("threeDigits", false);
        fourDigits = getIntent().getBooleanExtra("fourDigits", false);

        Random r = new Random();
        if (twoDigits) {
            randomNumber = r.nextInt(90) + 10;
        } else if (threeDigits) {
            randomNumber = r.nextInt(900) + 100;
        } else if (fourDigits) {
            randomNumber = r.nextInt(9000) + 1000;
        } else {
            randomNumber = r.nextInt(90) + 10;
        }

        textViewRight.setText(getString(R.string.guesses_left_format, guessesLeft));
        textViewLast.setText(getString(R.string.guess_title));
        hintTextView.setText(getString(R.string.enter_your_guess));

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessString = guessEditText.getText().toString();
                if (guessString.isEmpty()) {
                    Toast.makeText(GassingNumberGameActivity.this, getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int userGuess = Integer.parseInt(guessString);
                    guessesLeft--;
                    userAttempts++;
                    guessesList.add(userGuess);

                    textViewRight.setText(getString(R.string.guesses_left_format, guessesLeft));

                    if (userGuess == randomNumber) {
                        showResultDialog(true);
                    } else if (userGuess > randomNumber) {
                        hintTextView.setText(getString(R.string.guess_too_high, userGuess));
                    } else { // userGuess < randomNumber
                        hintTextView.setText(getString(R.string.guess_too_low, userGuess));
                    }

                    if (guessesLeft == 0 && userGuess != randomNumber) {
                        showResultDialog(false);
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(GassingNumberGameActivity.this, getString(R.string.invalid_number_format), Toast.LENGTH_SHORT).show();
                }

                guessEditText.setText("");
            }
        });
    }

    private void showResultDialog(boolean isWinner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GassingNumberGameActivity.this);
        builder.setTitle(getString(R.string.game_title));
        builder.setCancelable(false);

        if (isWinner) {
            builder.setMessage(getString(R.string.win_message, randomNumber, userAttempts, guessesList.toString()));
        } else {
            builder.setMessage(getString(R.string.lose_message, randomNumber, guessesList.toString()));
        }

        builder.setPositiveButton(getString(R.string.play_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GassingNumberGameActivity.this, GassingNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.create().show();
    }
}
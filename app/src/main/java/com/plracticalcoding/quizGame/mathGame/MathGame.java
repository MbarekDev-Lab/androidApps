package com.plracticalcoding.quizGame.mathGame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.plracticalcoding.myapplication.R;

import java.util.Random;

public class MathGame extends AppCompatActivity {
    TextView scoreText, lifeText, timeText, questionText;
    EditText answerInput;
    Button okButton, nextButton;

    int score = 0;
    int life = 3;
    int timeValue = 60;
    int correctAnswer;
    CountDownTimer timer;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);

        scoreText = findViewById(R.id.textView5);
        lifeText = findViewById(R.id.textView7);
        timeText = findViewById(R.id.textView9);
        questionText = findViewById(R.id.textView13);
        answerInput = findViewById(R.id.editTextText2);
        okButton = findViewById(R.id.button10);
        nextButton = findViewById(R.id.button11);

        random = new Random();
        gameContinue();

        okButton.setOnClickListener(view -> checkAnswer());
        nextButton.setOnClickListener(view -> gameContinue());
    }

    private void gameContinue() {
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);
        correctAnswer = num1 + num2;

        questionText.setText(num1 + " + " + num2 + " = ?");
        answerInput.setText("");

        startTimer();
    }

    private void startTimer() {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(timeValue * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeValue = (int) (millisUntilFinished / 1000);
                timeText.setText(String.valueOf(timeValue));
            }

            @Override
            public void onFinish() {
                life--;
                lifeText.setText(String.valueOf(life));
                if (life == 0) {
                    Toast.makeText(MathGame.this, "Game Over!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(MathGame.this, "Time’s up!", Toast.LENGTH_SHORT).show();
                    gameContinue();
                }
            }
        }.start();
    }

    private void checkAnswer() {
        String answerString = answerInput.getText().toString();
        if (answerString.isEmpty()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        int playerAnswer = Integer.parseInt(answerString);
        if (playerAnswer == correctAnswer) {
            score++;
            scoreText.setText(String.valueOf(score));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            gameContinue();
        } else {
            life--;
            lifeText.setText(String.valueOf(life));
            Toast.makeText(this, "Wrong! Correct: " + correctAnswer, Toast.LENGTH_SHORT).show();
            if (life == 0) {
                Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                gameContinue();
            }
        }
    }
}
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

import java.util.Locale;
import java.util.Random;

public class MathGame extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 10000;
    TextView scoreText, lifeText, timeText, questionText;
    EditText answerInput;
    Button okButton, nextButton;

    int score = 0;
    int life = 3;
    int timeValue = 60;
    int correctAnswer;
    CountDownTimer timer;
    Boolean timer_running;
    long time_left_in_millis = START_TIME_IN_MILLIS;
    int userLife;

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

        okButton.setOnClickListener(view -> {
            pauseTimer();
            checkAnswer();
        });
        nextButton.setOnClickListener(view -> {
            gameContinue();
            resetTimer();
            //updateTimer();
        });
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

        timer = new CountDownTimer(time_left_in_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_in_millis = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                timer_running = false;

                pauseTimer();
                resetTimer();
                updateTimer();

                userLife = userLife - 1;
                questionText.setText("Sorry! Time is up!");

            }
        }.start();
        timer_running = true;


        /*timer = new CountDownTimer(timeValue * 1000, 1000) {
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
        }.start();*/
    }

    private void resetTimer() {
    }

    private void pauseTimer() {
    }

    private void updateText() {
        int seconds = (int) (time_left_in_millis / 1000);
        String time = String.format(Locale.getDefault(), "%02d", seconds);
        timeText.setText(time);

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

    private void updateTimer() {
        timer_running = true;


    }

}
package com.plracticalcoding.quizGame.mathGame;

import android.content.Intent;
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
    int correctAnswer;
    CountDownTimer timer;
    boolean timer_running;
    long time_left_in_millis = START_TIME_IN_MILLIS;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_game);

        scoreText = findViewById(R.id.textViewScore);
        lifeText = findViewById(R.id.textViewLife);
        timeText = findViewById(R.id.textViewTime);
        questionText = findViewById(R.id.textViewQuestion);
        answerInput = findViewById(R.id.editTextAnswer);
        okButton = findViewById(R.id.buttonOk);
        nextButton = findViewById(R.id.buttonNext);

        random = new Random();
        gameContinue();

        okButton.setOnClickListener(view -> {
            pauseTimer();
            checkAnswer();
        });
        nextButton.setOnClickListener(view -> {
            answerInput.setText("");
            gameContinue();
            resetTimer();
        });
    }

    private void gameContinue() {
        okButton.setEnabled(true);
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);
        correctAnswer = num1 + num2;

        questionText.setText(num1 + " + " + num2 + " = ?");

        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

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
                life--;
                lifeText.setText(String.valueOf(life));
                questionText.setText("Sorry! Time is up!");
                okButton.setEnabled(false);

                if (life == 0) {
                    Toast.makeText(MathGame.this, "Game Over!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MathGame.this, MathResultActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
        timer_running = true;
    }

    private void resetTimer() {
        time_left_in_millis = START_TIME_IN_MILLIS;
        updateText();
    }

    private void pauseTimer() {
        if(timer != null) {
            timer.cancel();
            timer_running = false;
        }
    }

    private void updateText() {
        int seconds = (int) (time_left_in_millis / 1000) % 60;
        timeText.setText(String.format(Locale.getDefault(), "%02d", seconds));
    }

    private void checkAnswer() {
        String answerString = answerInput.getText().toString();
        if (answerString.isEmpty()) {
            Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
            startTimer(); // Restart timer if no answer is given
            return;
        }

        int playerAnswer = Integer.parseInt(answerString);
        resetTimer();

        if (playerAnswer == correctAnswer) {
            score++;
            scoreText.setText(String.valueOf(score));
            questionText.setText("Correct!");
            gameContinue();
        } else {
            life--;
            lifeText.setText(String.valueOf(life));
            questionText.setText("Wrong! Correct: " + correctAnswer);
            if (life == 0) {
                Toast.makeText(MathGame.this, "Game Over!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MathGame.this, MathResultActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            } else {
                gameContinue();
            }
        }
    }
}

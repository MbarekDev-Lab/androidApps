package com.plracticalcoding.number_guessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.plracticalcoding.myapplication.R;

public class GassingNumberSplash extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    Animation animationImg, animationTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplashctivity);

        textView = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView3);

        animationImg = AnimationUtils.loadAnimation(this, R.anim.image_animition_numgussing);
        animationTxt = AnimationUtils.loadAnimation(this, R.anim.text_animation_gussnum);

//        imageView.startAnimation(animationImg);
//        textView.startAnimation(animationTxt);
        imageView.setAnimation(animationTxt);
        textView.setAnimation(animationTxt);


        CountDownTimer task = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                startActivity(new Intent(GassingNumberSplash.this, GassingNumberActivity.class));
            }
        }.start();
        //task.start();
    }
}
package com.plracticalcoding.androidLibraries;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.plracticalcoding.androidLibraries.model.Info;
import com.plracticalcoding.myapplication.R;
import com.plracticalcoding.myapplication.databinding.ActivityMainDatabindingBinding;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class MainDatabindingActivity extends AppCompatActivity {
    ImageView imageView;
EditText editTextTextMessagef;
Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityMainDatabindingBinding binding = ActivityMainDatabindingBinding.inflate(getLayoutInflater());
        ActivityMainDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main_databinding);
        imageView = findViewById(R.id.imageView5);

        editTextTextMessagef = findViewById(R.id.editTextTextMessagef);
        sendButton = findViewById(R.id.sendButton);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentEventBus fragment = new FragmentEventBus();

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        Info infoData = new Info("Beni ", "Mbarek ", "Mr ");
        //  findViewById(R.id.data).setData(infoData);

        binding.setData(infoData);

        //setContentView(binding.getRoot());

        String imageView = "https://lh3.googleusercontent.com/2IQ9psSDzqfFk4o5aguHbYc5ee2iBLfzZddy0eXtAIvVbq9og11TBGQq8I2Ct7upyB4-FwEULww6gdMVGJjVqhxrxp87x5jE66lcYvgLBTF6MAN1bIbHvOOZQ8iOw55KXtuniboC";

        Picasso.get().load(imageView).into(this.imageView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userMessage = String.valueOf(editTextTextMessagef.getText());
                EventBus.getDefault().post(new MessageEvent(userMessage));



            }
        });

    }
}
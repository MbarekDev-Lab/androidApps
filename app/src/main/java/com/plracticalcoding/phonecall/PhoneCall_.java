package com.plracticalcoding.phonecall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

public class PhoneCall_ extends AppCompatActivity {

    EditText number;
    Button call;
    String userNunber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_call);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        call = findViewById(R.id.call);
        number = findViewById(R.id.num);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userNunber = number.getText().toString();
                phonecall(userNunber);
             }
        });
    }

    public void phonecall( String userNunber){
        if (ContextCompat.checkSelfPermission(PhoneCall_.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PhoneCall_.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+ userNunber));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+ userNunber));
            startActivity(intent);
        }
    }
}
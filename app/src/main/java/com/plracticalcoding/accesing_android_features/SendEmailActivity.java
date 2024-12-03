package com.plracticalcoding.accesing_android_features;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

public class SendEmailActivity extends AppCompatActivity {

    Button sendEmail;
    EditText adress, message, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sendEmail  = findViewById(R.id.sendEmail);
        message = findViewById(R.id.editTextemail);
        subject = findViewById(R.id.editTextsubject);
        adress = findViewById(R.id.editemailadress);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAdress = adress.getText().toString();
                String useMessag = message.getText().toString();
                String useSubject = subject.getText().toString();

                sendEmail(userAdress,useMessag,useSubject);

            }
        });
    }

    public  void  sendEmail(String userAdress, String useMessag, String useSubject) {
        String[]  emailAdress = {userAdress};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL,emailAdress);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,useSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, useMessag);

        startActivity(Intent.createChooser(emailIntent,"email sent"));
    }
}
package com.plracticalcoding.firbase_db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.plracticalcoding.myapplication.R;

import java.util.concurrent.TimeUnit;

public class PhoneSignActivity extends AppCompatActivity {

    EditText phoneNumber, smsCode;
    Button sendCode, signInWithphone;
    String codeSent;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phone_sign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        phoneNumber = findViewById(R.id.editTextPhone);//
        smsCode = findViewById(R.id.editTextCod);

        sendCode = findViewById(R.id.buttonsendsms);
        signInWithphone = findViewById(R.id.buttonSignin);

        sendCode.setOnClickListener(v -> {
            String userPhonenNum = phoneNumber.getText().toString();
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(userPhonenNum)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(PhoneSignActivity.this)
                    .setCallbacks(mCallBacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        });
        signInWithphone.setOnClickListener(v -> signWithPhoneCode());
    }

    public void signWithPhoneCode() {
        String enterUserCode = smsCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, enterUserCode);

        signinWithPhoneAuthCredential(credential);


    }

    public void signinWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(PhoneSignActivity.this, MainMenu.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(PhoneSignActivity.this, "The Code You entered  was not correct", Toast.LENGTH_SHORT).show();
                    }

                });

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }
    };
}
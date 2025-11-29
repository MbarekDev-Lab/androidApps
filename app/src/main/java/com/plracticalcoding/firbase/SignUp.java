package com.plracticalcoding.firbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.plracticalcoding.myapplication.R;

public class SignUp extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonSignup;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_new);

        editTextEmail = findViewById(R.id.editTextEmailSignup);
        editTextPassword = findViewById(R.id.editTextPasswordSignup);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = editTextEmail.getText().toString();
                String userPassword = editTextPassword.getText().toString();
                signUpFirbasr(userEmail, userPassword);
            }
        });
    }

    private void signUpFirbasr(String userEmail, String userPassword) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Your Account has been created", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SignUp.this, FirbaseMainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignUp.this, "THere is a problem with signing up", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}
package com.plracticalcoding.firbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.plracticalcoding.myapplication.databinding.ActivitySignupBinding;

public class SignUp extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = binding.editTextEmailSignup.getText().toString();
                String userPassword = binding.editTextPasswordSignup.getText().toString();
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
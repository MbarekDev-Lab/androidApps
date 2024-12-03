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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.plracticalcoding.myapplication.R;

public class SignUp extends AppCompatActivity {

    EditText editTextTextEmailAddress, editTextTextPassword;
    Button buttonEnter;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword2);
        buttonEnter = findViewById(R.id.buttonEnter);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = editTextTextEmailAddress.getText().toString();
                String userPassword = editTextTextPassword.getText().toString();
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

                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(SignUp.this, "THere is a problem with signing up", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}
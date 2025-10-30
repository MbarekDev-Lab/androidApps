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
import com.google.firebase.auth.FirebaseUser;
import com.plracticalcoding.myapplication.R;

public class MainActivity extends AppCompatActivity {
    EditText editTextTextEmailAddress, editTextTextPassword;
    Button buttonsignIn, buttonSignUp, buttonforgot, buttonphonenum;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        buttonsignIn = findViewById(R.id.buttonsignIn);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonforgot = findViewById(R.id.buttonforgot);
        buttonphonenum = findViewById(R.id.buttonphonenum);

        buttonSignUp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUp.class)));

        buttonsignIn.setOnClickListener(v -> {
            String userMail = editTextTextEmailAddress.getText().toString();
            String userPassword = editTextTextPassword.getText().toString();
            signInFirbase(userMail, userPassword);
        });

        buttonforgot.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPassActivity.class)));

        buttonphonenum.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PhoneSignActivity.class));
            finish();
        });
    }

    private void signInFirbase(String userMail, String userPassword) {
        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(MainActivity.this, MainMenu.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Mail or Password is not Correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, MainMenu.class);
            startActivity(intent);
            finish();
        }

    }
}
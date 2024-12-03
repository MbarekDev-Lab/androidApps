package com.plracticalcoding.Tack_Note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.plracticalcoding.myapplication.R;

public class AddNoteActivity extends AppCompatActivity {

    TextView title;
    EditText edTakbnote;
    Button btnCanc, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.texttitle);
        edTakbnote = findViewById(R.id.editTexttakenote);
        btnSave = findViewById(R.id.buttonsave);
        btnCanc = findViewById(R.id.buttoncancel);
        btnCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddNoteActivity.this, " blahhhh", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }

            private void saveNote() {
              String noteTitle = title.getText().toString();
              String noteDesc = edTakbnote.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("noteTitle",noteTitle);
                intent.putExtra("noteDesc", noteDesc);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

}
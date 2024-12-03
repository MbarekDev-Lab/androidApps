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

public class UpdateActivity extends AppCompatActivity {
    TextView title;
    EditText descNote;
    Button btnCanc, btnSave;
    int noteid ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        getSupportActionBar().setTitle("Edit Note");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.texttitleupdate);
        descNote = findViewById(R.id.editTexttakenoteupdate);
        btnSave = findViewById(R.id.buttonsaveupdate);
        btnCanc = findViewById(R.id.buttoncancelupdate);
        getUpdateNote();

        btnCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateActivity.this, " Note Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }

            private void updateNote() {
                String noteTitle = title.getText().toString();
                String noteDesc = descNote.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("noteTitleLast",noteTitle);
                intent.putExtra("noteDescLast", noteDesc);
                if (noteid != -1 ){
                    intent.putExtra("noteid", noteid);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    public void getUpdateNote(){
        Intent intent = getIntent();
         noteid = intent.getIntExtra("id",-1);
        String notetitle = intent.getStringExtra("title");
        String notedescription = intent.getStringExtra("descption");

        title.setText(notetitle);
        descNote.setText(notedescription);


    }
}
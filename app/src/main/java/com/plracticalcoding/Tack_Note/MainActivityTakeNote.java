package com.plracticalcoding.Tack_Note;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plracticalcoding.myapplication.R;

public class MainActivityTakeNote extends AppCompatActivity {
    NoteViewNodel noteViewNodel;

    ActivityResultLauncher<Intent> activityResultLauncherAddNote;
    ActivityResultLauncher<Intent> activityResultLauncherUpdateNote;

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data  != null ){
            String title = data.getStringExtra("noteTitle");
            String desc = data.getStringExtra("noteDesc");
            Note note = new Note(title, desc);
            noteViewNodel.insert(note);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MenuInflater inflater = getMenuInflater();
        setContentView(R.layout.activity_main_takingnote);
        registerActivityForAddNote();
        registerUpdate();

        RecyclerView recyclerView = findViewById(R.id.rvnotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewNodel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewNodel.class);
        // update Recycler View .
        noteViewNodel.getAllNotes().observe(this, adapter::setNotes);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //viewHolder.getAdapterPosition();
                noteViewNodel.delete(adapter.getNotes(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivityTakeNote.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickedListener(new NoteAdapter.OnItemClickedLisner() {
            @Override
            public void onItemClicked(Note note) {
                Intent intent = new Intent(MainActivityTakeNote.this, UpdateActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitel());
                intent.putExtra("description", note.getDescription());
                //activityResultLauncherUpdateNote
                activityResultLauncherUpdateNote.launch(intent);
            }
        });
    }

    public void registerUpdate() {
        activityResultLauncherUpdateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                int resultCode = o.getResultCode();
                Intent data = o.getData();

                if (resultCode == RESULT_OK && data != null) {
                    String title = data.getStringExtra("noteTitleLast");
                    String desc = data.getStringExtra("noteDescLast");
                    int id = data.getIntExtra("noteid", -1);

                    Note note = new Note(title, desc);
                    note.setId(id);

                    noteViewNodel.update(note);

                    /*
                    1) heutegerdatum
                    2) recksendunggrund
                    3) artikrelzustand
                    4) kdrsmitrs
                    5) versandkosten
                     */

                }


            }
        });
    }

    public void registerActivityForAddNote() {
        activityResultLauncherAddNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        int resultCode = o.getResultCode();
                        Intent data = o.getData();
                        if (resultCode == RESULT_OK && data != null) {
                            String title = data.getStringExtra("noteTitle");
                            String desc = data.getStringExtra("noteDesc");
                            Note note = new Note(title, desc);
                            noteViewNodel.insert(note);

                        }

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.top_menu) {
            Intent intent = new Intent(MainActivityTakeNote.this, AddNoteActivity.class);
            //startActivityForResult(intent,1);
            activityResultLauncherAddNote.launch(intent);
            //startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.plracticalcoding.Tack_Note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class NoteViewNodel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<Note>> notes;
    public NoteViewNodel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes   = repository.geAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return notes;
    }

}

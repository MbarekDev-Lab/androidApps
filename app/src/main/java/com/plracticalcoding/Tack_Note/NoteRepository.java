package com.plracticalcoding.Tack_Note;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final LiveData<List<Note>> notes;

    // executor service
    ExecutorService executorService  = Executors.newSingleThreadExecutor();
    public  NoteRepository(Application application){
        NoteDataBase dataBase = NoteDataBase.getInstance(application);
        noteDao = dataBase.noteDao();
        notes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        //new IsertNoteAsyncTask(noteDao).execute(note);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.insert(note);
            }
        });
    }
    public void update(Note note){
        //new UpdateNoteAsyncTask(noteDao).execute(note);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.update(note);
            }
        });
    }
    public void delete(Note note){
        //new DeleteNoteAsyncTask(noteDao).execute(note);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.delete(note);
            }
        });

    }
    public  LiveData<List<Note>> geAllNotes(){
        return  notes;

    }

    private static class IsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private final NoteDao noteDao;
        public IsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private final NoteDao noteDao;
        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private final NoteDao noteDao;
        public UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }



}

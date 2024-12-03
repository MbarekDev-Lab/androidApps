package com.plracticalcoding.Tack_Note;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase instance;
    public abstract NoteDao noteDao();

    public static synchronized  NoteDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,NoteDataBase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
           // new PopulateDbAsyncTask(instance).execute();
            NoteDao noteDao = instance.noteDao();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    noteDao.insert(new Note("Titale 1" , "Description 5 "));
                    noteDao.insert(new Note("Titale 2" , "Description 4 "));
                    noteDao.insert(new Note("Titale 3" , "Description 3 "));
                    noteDao.insert(new Note("Titale 4" , "Description 2 "));

                }
            });

        }
    };

    /*private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private final NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDataBase dataBase) {
            this.noteDao = dataBase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note("Titale 1" , "Description 5 "));
            noteDao.insert(new Note("Titale 2" , "Description 4 "));
            noteDao.insert(new Note("Titale 3" , "Description 3 "));
            noteDao.insert(new Note("Titale 4" , "Description 2 "));
            return null;
        }
    }*/



}

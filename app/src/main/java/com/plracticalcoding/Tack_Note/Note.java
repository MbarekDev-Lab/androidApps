package com.plracticalcoding.Tack_Note;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String titel;
    public String description;


    public Note( String titel, String description) {
        this.titel = titel;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
}

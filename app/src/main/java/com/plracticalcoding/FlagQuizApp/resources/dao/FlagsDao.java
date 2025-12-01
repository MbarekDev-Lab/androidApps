package com.plracticalcoding.FlagQuizApp.resources.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.plracticalcoding.FlagQuizApp.resources.datadase.DatabaseCopyHelper;
import com.plracticalcoding.FlagQuizApp.resources.model.FlagsModel;

import java.util.ArrayList;

public class FlagsDao {
    public ArrayList<FlagsModel> getRandomTenRecords(DatabaseCopyHelper helper){

        ArrayList<FlagsModel> recordList = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM flagquizgametable ORDER BY RANDOM() LIMIT 10",null);

        int idIndex = cursor.getColumnIndex("flag_id");
        int countryNameIndex = cursor.getColumnIndex("flag_name");
        int flagNameIndex = cursor.getColumnIndex("flag_image");

        while (cursor.moveToNext()){

            FlagsModel record = new FlagsModel(
                    cursor.getInt(idIndex),
                    cursor.getString(countryNameIndex),
                    cursor.getString(flagNameIndex)
            );

            recordList.add(record);
        }

        cursor.close();

        return recordList;
    }

    public ArrayList<FlagsModel> getRandomThreeRecords(DatabaseCopyHelper helper,int id){
        ArrayList<FlagsModel> recordList = new ArrayList<>();
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM flagquizgametable WHERE flag_id != ? ORDER BY RANDOM() LIMIT 3",new String[]{String.valueOf(id)});

        int idIndex = cursor.getColumnIndex("flag_id");
        int countryNameIndex = cursor.getColumnIndex("flag_name");
        int flagNameIndex = cursor.getColumnIndex("flag_image");

        while (cursor.moveToNext()){

            FlagsModel record = new FlagsModel(
                    cursor.getInt(idIndex),
                    cursor.getString(countryNameIndex),
                    cursor.getString(flagNameIndex)
            );

            recordList.add(record);
        }

        cursor.close();
        return recordList;
    }
}

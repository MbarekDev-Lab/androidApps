package com.example.todolist;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHelper {
    public static final String FILENAME = "listinfo.dat";

    public static void writeData(ArrayList<String> item, Context context) {
        try (FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readData(Context context) {
        ArrayList<String> itemsList = new ArrayList<>();
        try (FileInputStream fileInputStream = context.openFileInput(FILENAME);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            itemsList = (ArrayList<String>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            // This is expected on the first run, so we can just return an empty list.
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemsList;
    }
}

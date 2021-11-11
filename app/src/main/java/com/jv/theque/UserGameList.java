package com.jv.theque;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserGameList implements Serializable {

    private final Type GAME_TYPE = new TypeToken<List<Game>>() {
    }.getType();

    private ArrayList<Game> list = new ArrayList<Game>();


    public UserGameList() {
        list = loadListFromFile();
    }

    public ArrayList<Game> getUserGameList() {
        return list;
    }

    public void addGame(Game newGame) {

        if (!contains(newGame)) {
            list.add(newGame);
        }
    }

    public boolean contains(Game game) {
        return list.contains(game);
    }

    public void removeGame(Game gameToDelete) {
        if (list.contains(gameToDelete)) {
            list.remove(gameToDelete);
        }
    }

    public void clear() {
        list = new ArrayList<Game>();
    }

    private ArrayList<Game> loadListFromFile() {

        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(new File(App.getAppContext().getFilesDir(), "userGameList.json")));
            return new Gson().fromJson(String.valueOf(reader), GAME_TYPE);

        } catch (Exception e) {
            return new ArrayList<Game>();
        }
    }

    public void saveToFile() {

        try {
            String path = "userGameList.json";

            File myObj = new File(App.getAppContext().getFilesDir(), path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(myObj.getAbsoluteFile()));
            outputStreamWriter.write(String.valueOf(list));
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package com.jv.theque.GameImplementation;

import com.google.gson.reflect.TypeToken;
import com.jv.theque.App;
import com.jv.theque.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserGameList implements Serializable {

    private static final String storageFileName = "userGameList.bin";

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
        boolean cancelInsertion = false;
        for (Game tmpGame : list) {
            if (newGame.equals(tmpGame)) {
                cancelInsertion = true;
            }
        }

        if (!cancelInsertion) {
            list.add(newGame);
        }
        saveToFile();
    }

    public boolean contains(Game game) {
        return list.contains(game);
    }

    public void removeGame(Game gameToDelete) {
        if (list.contains(gameToDelete)) {
            list.remove(gameToDelete);
        }
        saveToFile();
    }

    public void clear() {
        list = new ArrayList<Game>();
    }

    private ArrayList<Game> loadListFromFile() {

        try {
            File myObj = new File(App.getAppContext().getFilesDir(), storageFileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileInputStream inputStream = new FileInputStream(myObj.getAbsoluteFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (ArrayList<Game>) objectInputStream.readObject();

        } catch (Exception e) {
            return new ArrayList<Game>();
        }
    }

    public void saveToFile() {

        try {

            File myObj = new File(App.getAppContext().getFilesDir(), storageFileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileOutputStream outputStream = new FileOutputStream(myObj.getAbsoluteFile());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package com.jv.theque;


import androidx.appcompat.app.AppCompatDelegate;

import com.jv.theque.favoritesImplementation.FavoriteSearchList;
import com.jv.theque.gameImplementation.UserGameList;
import com.jv.theque.tagsImplementation.CustomObserver;
import com.jv.theque.tagsImplementation.Tag;
import com.jv.theque.tagsImplementation.UserTagList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class UserData implements Serializable, CustomObserver {
    private static String storageFileName = "UserData.bin";
    private UserGameList userGameList;
    private UserTagList userTagList;
    private FavoriteSearchList userFavorites;
    private int themeMode;

    @Override
    public void update() {
        StringBuilder builder = new StringBuilder();
        builder.append("GameList size is now " + userGameList.getGameList().size());
        builder.append("\nTagList size is now " + userTagList.getList().size());
        builder.append("\nFavoriteList size is now " + userFavorites.getList().size());
        this.saveToFile();
    }

    public UserData() {
        loadListFromFile();
    }

    public UserGameList getUserGameList() {
        return userGameList;
    }

    public UserTagList getUserTagList() {
        return userTagList;
    }

    public FavoriteSearchList getUserFavorites() {
        return userFavorites;
    }

    public int getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(int themeMode) {
        this.themeMode = themeMode;
        saveToFile();
    }

    private void loadListFromFile() {
        try {
            File myObj = new File(App.getAppContext().getFilesDir(), storageFileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File " + myObj.getName() + " already exists.");
            }

            FileInputStream inputStream = new FileInputStream(myObj.getAbsoluteFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            UserData loadedUserData = (UserData) objectInputStream.readObject();

            //this.userGameList = loadedUserData.userGameList;
            this.userGameList = new UserGameList(this, loadedUserData.userGameList.getList());
            this.userTagList = new UserTagList(this, loadedUserData.userTagList.getList());
            this.userFavorites = new FavoriteSearchList(this, loadedUserData.userFavorites.getList());
            this.themeMode = loadedUserData.themeMode;

        } catch (Exception e) {
            this.userGameList = new UserGameList(this);
            this.userTagList = new UserTagList(this);
            this.userFavorites = new FavoriteSearchList(this);
            this.themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        return;
    }

    public void saveToFile() {

        try {
            //modifiée récemment pour essayer d'enlever le App, peut ne pas marcher
            File myObj = new File(App.getAppContext().getFilesDir(), storageFileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileOutputStream outputStream = new FileOutputStream(myObj.getAbsoluteFile());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

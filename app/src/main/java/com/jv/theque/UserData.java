package com.jv.theque;

import android.util.Log;

import com.jv.theque.favoritesImplementation.FavoriteSearchsList;
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
    private FavoriteSearchsList userFavorites;

    @Override
    public void update() {
        StringBuilder builder = new StringBuilder();
        builder.append("GameList size is now " + userGameList.getGameList().size());
        builder.append("\nTagList size is now " + userTagList.getList().size());
        builder.append("\nFavoriteList size is now " + userFavorites.getList().size());
        Log.i("MICHTOS", builder.toString());
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

    public FavoriteSearchsList getUserFavorites() {
        return userFavorites;
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
            this.userFavorites = new FavoriteSearchsList(this, loadedUserData.userFavorites.getList());

            Log.i("MICHTOS", "Loaded UserGameList with " + userGameList.getGameList().size() + " games inside");
            Log.i("MICHTOS", "Loaded UserTagList with " + userTagList.getList().size() + " tags inside");
            Log.i("MICTHOS", "Loaded USerFavorites with" + userFavorites.getList().size() + " searchs inside");

            for (Tag tag : userTagList.getList()) {
                Log.i("MICHTOS", tag.getName() + " : " + tag.getGames().size() + " jeux");
            }

        } catch (Exception e) {
            Log.e("MICHTOS", String.valueOf(e));
            this.userGameList = new UserGameList(this);
            this.userTagList = new UserTagList(this);
            this.userFavorites = new FavoriteSearchsList(this);
        }
        return;
    }

    public void saveToFile() {

        try {
            //modifiée récemment pour essayer d'enlever le App, peut ne pas marcher
            File myObj = new File(MainActivity.getContext().getFilesDir(), storageFileName);
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

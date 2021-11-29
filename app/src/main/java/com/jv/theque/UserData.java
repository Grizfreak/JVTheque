package com.jv.theque;

import android.util.Log;

import com.jv.theque.GameImplementation.UserGameList;
import com.jv.theque.TagsImplementation.CustomObserver;
import com.jv.theque.TagsImplementation.UserTagList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;


public class UserData implements Serializable, CustomObserver {
    private static String storageFileName = "UserData.bin";

    private UserGameList userGameList;
    private UserTagList userTagList;

    @Override
    public void update() {
        Log.i("MICHTOS", "salut");
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
            this.userGameList = loadedUserData.userGameList;
            this.userGameList = new UserGameList(this, loadedUserData.userGameList.getList());
            this.userTagList = new UserTagList(this, loadedUserData.userTagList.getList());

            Log.i("MICHTOS", "Loaded UserGameList with " + userGameList.getGameList().size() + " games inside");

        } catch (Exception e) {
            this.userGameList = new UserGameList(this);
            this.userTagList = new UserTagList(this);
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

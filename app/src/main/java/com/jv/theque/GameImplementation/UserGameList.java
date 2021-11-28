package com.jv.theque.GameImplementation;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.jv.theque.TagsImplementation.Tag;
import com.jv.theque.UserData;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class UserGameList extends Observable implements Serializable {

    private final Type GAME_TYPE = new TypeToken<List<Game>>() {
    }.getType();

    private List<Game> gameList = new ArrayList<Game>();


    public UserGameList(UserData userData) {
        addObserver(userData);
    }

    public UserGameList(UserData userData, List<Game> gameList) {
        addObserver(userData);
        this.gameList = gameList;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public synchronized void addGame(Game newGame) {
        boolean cancelInsertion = false;
        for (Game tmpGame : gameList) {
            if (newGame.equals(tmpGame)) {
                cancelInsertion = true;
            }
        }

        if (!cancelInsertion) {
            gameList.add(newGame);
        }
        notifyObservers();
        notify();
        notifyAll();
        Log.i("MICHTOS", "game added, "+ countObservers() + " Observers notified");
    }

    public boolean contains(Game game) {
        return gameList.contains(game);
    }

    public synchronized void removeGame(Game gameToDelete) {
        if (gameList.contains(gameToDelete)) {
            gameList.remove(gameToDelete);
            notifyObservers();
        }
        notifyObservers();
        notify();
        notifyAll();
    }

    public List<Game> getList() {
        return gameList;
    }
}

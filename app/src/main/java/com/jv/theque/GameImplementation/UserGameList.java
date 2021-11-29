package com.jv.theque.GameImplementation;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.jv.theque.TagsImplementation.CustomObservable;
import com.jv.theque.TagsImplementation.CustomObserver;
import com.jv.theque.TagsImplementation.Tag;
import com.jv.theque.UserData;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class UserGameList implements Serializable, CustomObservable {

    private final Type GAME_TYPE = new TypeToken<List<Game>>() {
    }.getType();

    private List<Game> gameList = new ArrayList<Game>();
    private List<CustomObserver> observerList = new ArrayList<CustomObserver>();

    public UserGameList(CustomObserver userData) {
        this.addObserver(userData);
    }

    public UserGameList(CustomObserver userData, List<Game> gameList) {
        this.addObserver(userData);
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
        this.notifyObserver();
    }

    public boolean contains(Game game) {
        return gameList.contains(game);
    }

    public synchronized void removeGame(Game gameToDelete) {
        if (gameList.contains(gameToDelete)) {
            gameList.remove(gameToDelete);
            for(Tag t : gameToDelete.getTags()){
                t.removeGame(gameToDelete);
            }
        }
        this.notifyObserver();
    }

    public List<Game> getList() {
        return gameList;
    }

    @Override
    public void addObserver(CustomObserver o) {
        observerList.add(o);
    }

    @Override
    public void notifyObserver() {
        for(CustomObserver o : observerList){
            o.update();
        }
    }
}

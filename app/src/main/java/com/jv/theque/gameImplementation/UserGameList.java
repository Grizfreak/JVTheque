package com.jv.theque.gameImplementation;


import com.jv.theque.tagsImplementation.CustomObservable;
import com.jv.theque.tagsImplementation.CustomObserver;
import com.jv.theque.tagsImplementation.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserGameList implements Serializable, CustomObservable, CustomObserver {

    private List<Game> gameList = new ArrayList<>();
    private final List<CustomObserver> observerList = new ArrayList<>();

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
                break;
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

    public Game find(String slug) {
        for (Game game : gameList) {
            if (game.getSlug().equals(slug)) {
                return game;
            }
        }
        return null;
    }

    public synchronized void removeGame(Game gameToDelete) {
        if (gameList.contains(gameToDelete)) {
            gameList.remove(gameToDelete);
            for (Tag t : gameToDelete.getTags()) {
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

    private void notifyObserver() {
        for (CustomObserver o : observerList) {
            o.update();
        }
    }

    @Override
    public void update() {
        notifyObserver();
    }
}

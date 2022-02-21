package com.jv.theque.favoritesImplementation;

import com.jv.theque.UserData;
import com.jv.theque.tagsImplementation.CustomObservable;
import com.jv.theque.tagsImplementation.CustomObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteSearchList implements CustomObservable, Serializable {
    private final List<FavoriteSearch> searchList;

    private final List<CustomObserver> observerList = new ArrayList<>();

    public FavoriteSearchList(UserData userData) {
        this(userData, new ArrayList<>());
    }

    public FavoriteSearchList(UserData userData, List<FavoriteSearch> searchList) {
        addObserver(userData);
        this.searchList = searchList;
    }

    public List<FavoriteSearch> getList() {
        return searchList;
    }

    public synchronized void add(FavoriteSearch search) {
        if (searchList.contains(search)) {
            return;
        }
        searchList.add(search);
        notifyObserver();
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
}
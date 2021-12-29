package com.jv.theque.favoritesImplementation;

import android.util.Log;

import com.jv.theque.UserData;
import com.jv.theque.tagsImplementation.CustomObservable;
import com.jv.theque.tagsImplementation.CustomObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteSearchsList implements CustomObservable, Serializable {
    private List<FavoriteSearch> searchList;

    private List<CustomObserver> observerList = new ArrayList<CustomObserver>();

    public FavoriteSearchsList(UserData userData) {
        this(userData, new ArrayList<>());
    }

    public FavoriteSearchsList(UserData userData, List<FavoriteSearch> searchList) {
        addObserver(userData);
        this.searchList = searchList;
    }

    public List<FavoriteSearch> getList() {
        return searchList;
    }

    public synchronized boolean add(FavoriteSearch search) {
        if (searchList.contains(search)) {
            return false;
        }
        searchList.add(search);
        notifyObserver();
        return true;
    }

    public synchronized boolean remove(FavoriteSearch search) {
        //empêche le retrait de la liste d'un tag qui n'existe pas ou d'un tag fourni par l'API
        if (!searchList.contains(search)) {
            return false;
        }
        searchList.remove(search);
        notifyObserver();
        return true;
    }

    @Override
    public void addObserver(CustomObserver o) {
        observerList.add(o);
    }

    @Override
    public void notifyObserver() {
        Log.i("MICHTOS", "update la liste des recherches favorites !!");
        for (CustomObserver o : observerList) {
            o.update();
        }
    }
}
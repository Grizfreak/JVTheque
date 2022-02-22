package com.jv.theque.tagsImplementation;

import com.jv.theque.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserTagList implements Serializable, CustomObservable, CustomObserver {
    private List<Tag> tagList = new ArrayList<>();
    private final List<CustomObserver> observerList = new ArrayList<>();

    public UserTagList(CustomObserver userData) {
        this.addObserver(userData);
    }

    public UserTagList(UserData userData, List<Tag> tagList) {
        addObserver(userData);
        this.tagList = tagList;
    }

    public List<Tag> getList() {
        return tagList;
    }

    public Tag find(String name) {
        for (Tag tag : tagList) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }

        return null;
    }

    public synchronized void add(Tag tag) {
        if (tagList.contains(tag)) {
            return;
        }
        tagList.add(tag);
        notifyObserver();
    }

    public synchronized void remove(Tag tag) {
        //empêche le retrait de la liste d'un tag qui n'existe pas ou d'un tag fourni par l'API
        if (!tagList.contains(tag)) {
            return;
        }
        tagList.remove(tag);
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

    @Override
    public synchronized void update() {
        notifyObserver();
    }

    public List<String> getTagNameList(){
     List<String> result = new ArrayList<>();
     for (Tag tag : tagList){
         if (tag instanceof UserTag){
             result.add(tag.getName());
         }
     }
     return result;
    }
}

package com.jv.theque.tagsImplementation;

import android.graphics.Color;

import com.jv.theque.gameImplementation.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Tag extends CustomObservable {


    HashMap<TagType, String> SpecialChars = new HashMap<TagType, String>() {{
        put(TagType.RAWGTAG, "\u200e");
        put(TagType.USERTAG, "\u200f");
    }};

    enum TagType {
        RAWGTAG,
        USERTAG
    }

    List<CustomObserver> customObserverList = new ArrayList<>();

    void addObserver(CustomObserver o);

    void notifyObserver();

    TagType getType();

    int color = Color.BLACK;

    default void setColor(int color) {
        return;
    }

    default void setName(String name) {
        return;
    }

    String getName();

    int getColor();

    List<Game> getGames();

    void addGame(Game game);

    void removeGame(Game game);

}

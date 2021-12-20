package com.jv.theque.TagsImplementation;

import android.graphics.Color;

import com.jv.theque.GameImplementation.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Tag extends CustomObservable {


    HashMap<TagType, String> SpecialChars = new HashMap<TagType, String>() {{
        put(TagType.RAWGTAG, "\u200e");
        put(TagType.USERTAG, "\u200f");
    }};

    public enum TagType {
        RAWGTAG,
        USERTAG
    }

    public List<CustomObserver> customObserverList = new ArrayList<CustomObserver>();

    public void addObserver(CustomObserver o);

    public void notifyObserver();

    public TagType getType();

    public int color = Color.BLACK;

    public default void setColor(int color) {
        return;
    }

    ;

    public default void setName(String name) {
        return;
    }

    ;

    public String getName();

    public int getColor();

    public List<Game> getGames();

    public void addGame(Game game);

    public void removeGame(Game game);

}

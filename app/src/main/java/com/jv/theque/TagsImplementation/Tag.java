package com.jv.theque.TagsImplementation;

import android.graphics.Color;

import com.jv.theque.GameImplementation.Game;

import java.util.List;

public interface Tag {
    public enum TagType {
        RAWGTAG,
        USERTAG
    }

    //TODO BECOME OBSERVABLE
    public TagType getType();

    public int color = Color.BLACK;

    public default void setColor(int color){
        return ;
    };

    public default void setName(String name){
        return;
    };

    public String getName();

    public int getColor();

    public List<Game> getGames();

    public void addGame(Game game);


}

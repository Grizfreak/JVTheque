package com.jv.theque.TagsImplementation;

import android.graphics.Color;

import com.jv.theque.GameImplementation.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserTag implements Tag, Serializable {
    private String name;
    private int color;
    private List<Game> games;
    private TagType type = TagType.USERTAG;

    public UserTag(String name) {
        this.name = name;
        this.games = new ArrayList<Game>();
        this.color = Tag.color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public TagType getType() {
        return type;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public List<Game> getGames() {
        return games;
    }

    @Override
    public void addGame(Game game) {
        games.add(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTag userTag = (UserTag) o;
        return name.equals(userTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

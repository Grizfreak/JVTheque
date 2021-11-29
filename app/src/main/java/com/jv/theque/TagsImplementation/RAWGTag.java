package com.jv.theque.TagsImplementation;

import com.jv.theque.GameImplementation.Game;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGTags;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RAWGTag implements Tag, Serializable {


    private String name;
    private int color;
    private List<Game> games = new ArrayList<Game>();
    private TagType type = TagType.RAWGTAG;

    public RAWGTag(String name, Game game) {
        this.name = name;
        this.color = Tag.color;
        games.add(game);
    }

    @Override
    public TagType getType() {
        return type;
    }

    @Override
    public void setColor(int color) {
        return;
    }

    @Override
    public void setName(String name) {
        return;
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
        RAWGTag rawgTag = (RAWGTag) o;
        return name.equals(rawgTag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return this.getName();
    }


}

package com.jv.theque.TagsImplementation;

import com.jv.theque.GameImplementation.Game;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGTags;

import java.util.ArrayList;
import java.util.List;

public class RAWGTag implements Tag {

    private String name;
    private int color;
    private List<Game> games;
    private TagType type = TagType.RAWGTAG;

    public RAWGTag(String name) {
        this.name = name;
        this.games = new ArrayList<Game>();
        this.color = Tag.color;
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
}

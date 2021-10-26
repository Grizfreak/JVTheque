package com.jv.theque;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    private String name;
    private List<Game> games = new ArrayList<>();

    public Tag(String name, Game game){
        this.name = name;
        games.add(game);
    }

    public String getName() {
        return name;
    }

    public List<Game> getGames() {
        return games;
    }
}

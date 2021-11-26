package com.jv.theque.GameImplementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tag implements Serializable {
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

    @Override
    public String toString(){

        return this.name;
    }
}

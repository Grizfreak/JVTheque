package com.jv.theque.TagsImplementation;

import com.jv.theque.GameImplementation.Game;
import com.jv.theque.MainActivity;

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
        addObserver(MainActivity.userData.getUserTagList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void addObserver(CustomObserver o) {
        customObserverList.add(o);
    }

    @Override
    public void notifyObserver() {
        for (CustomObserver o : customObserverList) {
            o.update();
        }
    }

    @Override
    public TagType getType() {
        return type;
    }

    @Override
    public List<Game> getGames() {
        return games;
    }

    @Override
    public synchronized void addGame(Game game) {
        games.add(game);
        notifyObserver();
    }

    @Override
    public synchronized void removeGame(Game game) {
        games.remove(game);
        if(games.size() == 0){
            MainActivity.userData.getUserTagList().remove(this);
        }
        notifyObserver();
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

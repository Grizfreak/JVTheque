package com.jv.theque.tagsImplementation;

import androidx.annotation.NonNull;

import com.jv.theque.gameImplementation.Game;
import com.jv.theque.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserTag implements Tag, Serializable {
    private final String name;
    private int color;
    private final List<Game> games;
    private final TagType type = TagType.USERTAG;

    public UserTag(String name) {
        this.name = name;
        this.games = new ArrayList<>();
        this.color = Tag.color;
        addObserver(MainActivity.userData.getUserTagList());
    }

    @Override
    public String getName() {
        return SpecialChars.get(getType()) + name;
    }

    @Override
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void addObserver(CustomObserver o) {
        customObserverList.add(o);
    }

    private void notifyObserver() {
        for (CustomObserver o : customObserverList) {
            o.update();
        }
    }

    @Override
    public TagType getType() {
        return type;
    }

    @Override
    public synchronized void addGame(Game game) {
        games.add(game);
        notifyObserver();
    }

    @Override
    public synchronized void removeGame(Game game) {
        games.remove(game);
        if (games.size() == 0) {
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

    @NonNull
    @Override
    public String toString() {
        return "UserTag{name='" + name + "'}";
    }
}

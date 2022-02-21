package com.jv.theque.tagsImplementation;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.jv.theque.gameImplementation.Game;
import com.jv.theque.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RAWGTag implements Tag, Serializable {


    private final String name;
    private final int color;
    private final List<Game> games = new ArrayList<>();
    private final TagType type = TagType.RAWGTAG;
    private final List<CustomObserver> observerList = new ArrayList<>();

    public RAWGTag(String name, Game game) {
        this.name = name;
        String realName;
        if (name.contains("\u200e")) realName = name.substring(1);
        else realName = name;
        switch (realName){
            case "macOS":
            case "Linux":
            case "Web":
            case "PC":
                color = Color.argb(255,67, 67, 69);  break;
            case "Xbox":
            case "Xbox 360":
            case "Xbox One":
            case "Xbox Series S/X":
                color = Color.argb(255,0, 126, 0);  break;
            case "PSP":
            case "PS Vita":
            case "PlayStation 2":
            case "PlayStation 3":
            case "PlayStation 4":
            case "PlayStation 5":
                color = Color.argb(255,0, 67, 156);  break;
            case "Game Boy":
            case "Game Boy Advance":
            case "NES":
            case "SNES":
            case "Nintendo 64":
            case "GameCube":
            case "Nintendo DS":
            case "Nintendo 3DS":
            case "Wii":
            case "Wii U":
            case "Nintendo Switch":
                color = Color.argb(255,230, 0, 18);  break;
            case "iOS":
            case "Android":
                color = Color.argb(255,122, 106, 62);  break;
            default:
                color = Color.GRAY;
                break;
        }
        games.add(game);
    }

    @Override
    public TagType getType() {
        return type;
    }

    @Override
    public String getName() {
        return SpecialChars.get(getType()) + name;
    }

    @Override
    public void setName(String name) {
        return;
    }   // Empêche de modifier le nom d'un tag de l'API

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        return;
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
        if (games.size() == 0) {
            MainActivity.userData.getUserTagList().remove(this);
        }
        notifyObserver();
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

    @NonNull
    @Override
    public String toString() {
        return this.getName();
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
}

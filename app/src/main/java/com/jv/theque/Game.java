package com.jv.theque;

import com.jv.theque.RAWGImplementation.RAWGGame;
import com.jv.theque.RAWGImplementation.RAWGGenres;
import com.jv.theque.RAWGImplementation.RAWGPlatform;
import com.jv.theque.RAWGImplementation.RAWGPlatformsList;
import com.jv.theque.RAWGImplementation.RAWGStoresList;
import com.jv.theque.RAWGImplementation.RAWGTags;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private String slug;
    private int id;
    private String name;
    private Date release_date;
    private Map<String, List<Tag>> tags;
    private String backgroundImageLink;

    public Game(RAWGGame game) {
        this.slug = game.slug;
        this.id = Integer.parseInt(game.id);
        this.name = game.name;
        this.release_date = game.releaseDate;
        this.tags = new HashMap<String, List<Tag>>();
        //tags = this.setupTags(game);
        this.backgroundImageLink = game.backgroundImageLink;

    }

    private Map<String, List<Tag>> setupTags(RAWGGame game) {
        Map tagMap = new HashMap<String, List<Tag>>();
        ArrayList<Tag> tags = new ArrayList<Tag>();

        //TODO Tags / Genres / Platform / Stores
        tagMap.put("tag",new ArrayList<Tag>());
        tagMap.put("genre",new ArrayList<Tag>());
        tagMap.put("platform",new ArrayList<Tag>());
        tagMap.put("store",new ArrayList<Tag>());

        //Récupération des tags du jeu
        for (RAWGTags tag : game.tags) {
            Tag tag1 = new Tag();
        }
        //Récupération des genres

        for (RAWGGenres tag : game.genres) {

        }
        //Récupération des plateformes
        for (RAWGPlatformsList tag : game.platforms) {

        }
        //Récupération des magasins
        for (RAWGStoresList tag : game.stores) {

        }
        return tagMap;
    }

    public String getSlug() {
        return slug;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public Map<String, List<Tag>> getTags() {
        return tags;
    }

    public String getBackgroundImageLink() {
        return backgroundImageLink;
    }
}

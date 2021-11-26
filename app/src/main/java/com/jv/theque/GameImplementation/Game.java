package com.jv.theque.GameImplementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGGame;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGPlatformsList;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGStoresList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game implements Serializable {
    private String slug;
    private int id;
    private String name;
    private Date release_date;
    private Map<String, List<Tag>> tags;
    private String backgroundImageLink;
    private String description;

    public Game(RAWGGame game) {
        this.slug = game.slug;
        this.id = Integer.parseInt(game.id);
        this.name = game.name;
        this.release_date = game.releaseDate;
        this.tags = new HashMap<String, List<Tag>>();
        tags = this.setupTags(game);
        this.backgroundImageLink = game.backgroundImageLink;
        this.description = null;

    }

    private Map<String, List<Tag>> setupTags(RAWGGame game) {
        Map tagMap = new HashMap<String, List<Tag>>();

        //TODO Tags / Genres / Platform / Stores
        tagMap.put("Usertag", new ArrayList<Tag>());
        tagMap.put("platform", new ArrayList<Tag>());
        tagMap.put("store", new ArrayList<Tag>());

        //Récupération des plateformes
        if (game.platforms != null) {
            for (RAWGPlatformsList tag : game.platforms) {
                Tag tag1 = new Tag(tag.RAWGPlatform.name, this);
                ((ArrayList) tagMap.get("platform")).add(tag1);
            }
        }
        //Récupération des magasins
        if (game.stores != null) {
            for (RAWGStoresList tag : game.stores) {
                Tag tag1 = new Tag(tag.RAWGStore.name, this);
                ((ArrayList) tagMap.get("store")).add(tag1);
            }

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

    //TODO Return tag of each Category
    public ArrayList<Tag> getPlatforms(){
        return (ArrayList<Tag>) tags.get("platform");
    }

    public ArrayList<Tag> getStores(){
        return (ArrayList<Tag>) tags.get("store");
    }

    public String getBackgroundImageLink() {
        return backgroundImageLink;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString() {

        JsonObject gameObject = new JsonObject();
        gameObject.addProperty("slug", slug);
        gameObject.addProperty("id", id);
        gameObject.addProperty("name", name);
        gameObject.addProperty("release_date", String.valueOf(release_date));
        gameObject.addProperty("backgroundImageLink", backgroundImageLink);

        JsonObject tagObject = new JsonObject();

        JsonArray tempTagSubArray = new JsonArray();
        for(Tag tag : tags.get("tag")){
            tempTagSubArray.add(tag.toString());
        }
        tagObject.add("tag", tempTagSubArray);

        tempTagSubArray = new JsonArray();
        for(Tag tag : tags.get("genre")){
            tempTagSubArray.add(tag.toString());
        }
        tagObject.add("genre", tempTagSubArray);

        tempTagSubArray = new JsonArray();
        for(Tag tag : tags.get("platform")){
            tempTagSubArray.add(tag.toString());
        }
        tagObject.add("platform", tempTagSubArray);

        tempTagSubArray = new JsonArray();
        for(Tag tag : tags.get("store")){
            tempTagSubArray.add(tag.toString());
        }
        tagObject.add("store", tempTagSubArray);

        gameObject.add("tags", tagObject);

        return gameObject.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && slug.equals(game.slug) && name.equals(game.name) && backgroundImageLink.equals(game.backgroundImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug, id, name, release_date, tags, backgroundImageLink);
    }
}

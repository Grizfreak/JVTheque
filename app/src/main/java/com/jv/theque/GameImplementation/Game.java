package com.jv.theque.GameImplementation;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jv.theque.MainActivity;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGGame;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGPlatformsList;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGStoresList;
import com.jv.theque.TagsImplementation.RAWGTag;
import com.jv.theque.TagsImplementation.Tag;
import com.jv.theque.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game implements Serializable {
    public final static Date DEFAULT_DATE = new Date(0, 0, 1); // Date par défaut, renvoyée dans le cas où le jeu n'en comporte pas

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
        this.tags = new HashMap<>();
        tags = this.setupTags();
        setupRAWGTags(game);
        this.backgroundImageLink = game.backgroundImageLink;
        this.description = null;

    }

    private Map<String, List<Tag>> setupTags() {
        Map tagMap = new HashMap<String, List<Tag>>();
        tagMap.put("Usertag", new ArrayList<Tag>());
        tagMap.put("platform", new ArrayList<Tag>());
        tagMap.put("store", new ArrayList<Tag>());
        return tagMap;
    }


    private void setupRAWGTags(RAWGGame game) {
        //Récupération des plateformes
        if (game.platforms != null) {
            for (RAWGPlatformsList tag : game.platforms) {
                Log.e("ingameRAWG", tag.RAWGPlatform.name + " " + game.name);
                Tag tag1 = new RAWGTag(tag.RAWGPlatform.name, this);
                tags.get("platform").add(tag1);
            }
        }
        //Récupération des magasins
        if (game.stores != null) {
            for (RAWGStoresList tag : game.stores) {
                Tag tag1 = new RAWGTag(tag.RAWGStore.name, this);
                ((ArrayList) tags.get("store")).add(tag1);
            }

        }
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
        if (release_date != null) {
            return release_date;
        } else {
            return DEFAULT_DATE;
        }
    }

    //TODO Return tag of each Category
    public ArrayList<Tag> getPlatforms() {
        Log.e("envoi", "envoi de " + this.tags.get("platform").size() + " tags de plateforme pour le jeu : " + this.name);
        for (Tag t : this.tags.get("platform")) {
            Log.e("ingame", t.getName());
        }
        return (ArrayList<Tag>) this.tags.get("platform");
    }

    public ArrayList<Tag> getUserTags() {
        Log.e("envoi", "envoi de " + this.tags.get("Usertag").size() + " tags custom pour le jeu : " + this.name);
        for (Tag t : this.tags.get("Usertag")) {
            Log.e("ingame", t.getName());
        }
        return (ArrayList<Tag>) this.tags.get("Usertag");
    }

    public List<Tag> getTags() {
        List<Tag> tmpList = new ArrayList<Tag>();

        List<String> keys = new ArrayList<>(tags.keySet());

        for (String value : keys) {

            List<Tag> tmpTagList = tags.get(value);

            for (Tag t : tmpTagList) {
                tmpList.add(t);
            }

        }

        return tmpList;
    }

    public ArrayList<Tag> getStores() {
        return (ArrayList<Tag>) tags.get("store");
    }

    public String getBackgroundImageLink() {
        return backgroundImageLink;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        /*JsonObject gameObject = new JsonObject();
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

        return gameObject.toString();*/
        return "";

    }

    public void addUserTagtoList(Tag tag) {


        if (getTags().contains(tag)) {
            return;
        } else {
            tag.addGame(this);
            tags.get("Usertag").add(tag);
            Log.e("Fesse", tag.getName());
        }
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

    public void addTagsToList() {
        //TODO store tags in UserData and tout court enfait parce que je l'ai pas fait
        List<Tag> tmpTagList = new ArrayList<Tag>();
        for (Tag tag : this.tags.get("platform")) {
            if (MainActivity.userData.getUserTagList().getList().contains(tag)) {
                tmpTagList.add(MainActivity.userData.getUserTagList().find(tag.getName()));
                MainActivity.userData.getUserTagList().find(tag.getName()).addGame(this);
            } else {
                tmpTagList.add(tag);
                tag.addObserver(MainActivity.userData.getUserTagList());
                MainActivity.userData.getUserTagList().add(tag);
            }
        }

        this.tags.put("platform", tmpTagList);
    }

    public void setRAWGTags(List<Tag> toAdd) {
        tags.put("platform", toAdd);
    }
}

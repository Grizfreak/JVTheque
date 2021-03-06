package com.jv.theque.gameImplementation;

import androidx.annotation.NonNull;

import com.jv.theque.MainActivity;
import com.jv.theque.rawgImplementation.serializableGame.*;
import com.jv.theque.tagsImplementation.RAWGTag;
import com.jv.theque.tagsImplementation.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Game implements Serializable {
    public final static Date DEFAULT_DATE = new Date(0, 0, 1); // Date par défaut, renvoyée dans le cas où le jeu n'en comporte pas

    private final String slug;
    private final int id;
    private final String name;
    private final Date release_date;
    private Map<String, List<Tag>> tags;
    private final String backgroundImageLink;
    private String description;
    private int note;

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
        this.note = -1;
    }

    private Map<String, List<Tag>> setupTags() {
        Map<String, List<Tag>> tagMap = new HashMap<>();
        tagMap.put("Usertag", new ArrayList<>());
        tagMap.put("platform", new ArrayList<>());
        tagMap.put("store", new ArrayList<>());
        return tagMap;
    }

    private void setupRAWGTags(RAWGGame game) {
        //Récupération des plateformes
        if (game.platforms != null) {
            for (RAWGPlatformsList tag : game.platforms) {
                Tag tag1 = new RAWGTag(tag.RAWGPlatform.name, this);
                Objects.requireNonNull(tags.get("platform")).add(tag1);
            }
        }
        //Récupération des magasins
        if (game.stores != null) {
            for (RAWGStoresList tag : game.stores) {
                Tag tag1 = new RAWGTag(tag.RAWGStore.name, this);
                (Objects.requireNonNull(tags.get("store"))).add(tag1);
            }

        }
    }

    public String getSlug() {
        return slug;
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

    public ArrayList<Tag> getPlatforms() {
        return (ArrayList<Tag>) this.tags.get("platform");
    }

    public ArrayList<Tag> getUserTags() {
        return (ArrayList<Tag>) this.tags.get("Usertag");
    }

    public List<Tag> getTags() {
        List<Tag> tmpList = new ArrayList<>();

        List<String> keys = new ArrayList<>(tags.keySet());

        for (String value : keys) {

            List<Tag> tmpTagList = tags.get(value);

            tmpList.addAll(Objects.requireNonNull(tmpTagList));

        }

        return tmpList;
    }

    public String getBackgroundImageLink() {
        return backgroundImageLink;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        description = description.replaceAll("<br/>", "\n");
        description = description.replaceAll("<br>", "\n");
        description = description.replaceAll("<h1>", " ");
        description = description.replaceAll("<h2>", " ");
        description = description.replaceAll("<h3>", " ");
        description = description.replaceAll("</h1>", " ");
        description = description.replaceAll("</h2>", " ");
        description = description.replaceAll("</h3>", " ");
        description = description.replaceAll("<ul>", "\n");
        description = description.replaceAll("</ul>", "\n");
        description = description.replaceAll("<li>", "- ");
        description = description.replaceAll("</li>", "\n");
        description = description.replaceAll("<strong>", "");
        description = description.replaceAll("</strong>", "");
        description = description.replaceAll("&#39;", "'");
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " avec " + Objects.requireNonNull(tags.get("Usertag")).size() + " tags custom";
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
        List<Tag> tmpTagList = new ArrayList<>();
        for (Tag tag : Objects.requireNonNull(this.tags.get("platform"))) {
            // Si le tag est dans la liste
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

    public void addTag(Tag tag) {
        List<Tag> tagList;
        String listName;
        switch (tag.getType()) {
            case RAWGTAG: {
                listName = "platform";
                break;
            }
            case USERTAG: {
                listName = "Usertag";
                break;
            }
            default: {
                throw new IllegalStateException("tagType " + tag.getType() + " does not exist");
            }
        }
        tagList = tags.get(listName);

        if (!Objects.requireNonNull(tagList).contains(tag)) {

            if (MainActivity.userData.getUserTagList().getList().contains(tag)) {
                tagList.add(MainActivity.userData.getUserTagList().find(tag.getName()));
                MainActivity.userData.getUserTagList().find(tag.getName()).addGame(this);
            } else {
                tag.addObserver(MainActivity.userData.getUserTagList());
                MainActivity.userData.getUserTagList().add(tag);
                tag.addGame(this);
                tagList.add(tag);

            }
            tags.put(listName, tagList);
            MainActivity.userData.saveToFile();

        }
    }

    public void setRAWGTags(List<Tag> toAdd) {
        tags.put("platform", toAdd);
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}

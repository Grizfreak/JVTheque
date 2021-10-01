package com.jv.theque;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Game {

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("released")
    @Expose
    public Date releaseDate;

    @SerializedName("background_image")
    @Expose
    public String backgroundImageLink;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("rating")
    @Expose
    public float rating;

    @SerializedName("tags")
    @Expose
    public Tags[] tags;

    @SerializedName("genres")
    @Expose
    public Genres[] genres;

    @SerializedName("platforms")
    @Expose
    public Platforms[] platforms;

    @SerializedName("stores")
    @Expose
    public Stores[] stores;

    @NonNull
    @Override
    public String toString() {
        StringBuilder tags_string= new StringBuilder();
        StringBuilder genres_string= new StringBuilder();
        StringBuilder platforms_string= new StringBuilder();
        StringBuilder stores_string= new StringBuilder();
        for (Tags tag : tags){
            tags_string.append(tag.toString());
        }
        for (Genres genre : genres){
            genres_string.append(genre.toString());
        }
        for (Platforms plat : platforms){
            platforms_string.append(plat.toString());
        }
        for (Stores str : stores){
            stores_string.append(str.toString());
        }
        return "Slug : "+slug+"\n"+
                "Name : "+name+"\n"+
                "Date : "+releaseDate+"\n"+
                "Background Image Link : "+backgroundImageLink+"\n"+
                "id : "+id+"\n"+
                "rating : "+rating+"\n"+
                "Tags : "+tags_string+"\n"+
                "Genres : "+genres_string+"\n"+
                "Platforms : "+platforms_string+"\n"+
                "Stores : "+stores_string+"\n";
    }
}

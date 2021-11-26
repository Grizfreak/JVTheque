package com.jv.theque.RAWGImplementation.SerializableGame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RAWGGame {

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
    public RAWGTags[] tags;

    @SerializedName("genres")
    @Expose
    public RAWGGenres[] genres;

    @SerializedName("platforms")
    @Expose
    public RAWGPlatformsList[] platforms;

    @SerializedName("stores")
    @Expose
    public RAWGStoresList[] stores;

    @NonNull
    @Override
    public String toString() {
        StringBuilder tags_string= new StringBuilder();
        StringBuilder genres_string= new StringBuilder();
        StringBuilder platforms_string= new StringBuilder();
        StringBuilder stores_string= new StringBuilder();
        if(tags != null) {
            for (RAWGTags tag : tags) {
                tags_string.append(tag.toString());
            }
        }
        if(genres != null) {
            for (RAWGGenres genre : genres) {
                genres_string.append(genre.toString());
                Log.v("DEBUG_JSON", genre.toString());;
            }
        }
        if(platforms != null) {
            for (RAWGPlatformsList plat : platforms) {
                platforms_string.append(plat.toString());
            }
        }
        if(stores != null){
            for (RAWGStoresList str : stores){
                stores_string.append(str.toString());
            }
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


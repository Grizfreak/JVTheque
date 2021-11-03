package com.jv.theque.RAWGImplementation;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RAWGTags {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("slug")
    @Expose
    public String slug;

    @SerializedName("language")
    @Expose
    public String language;

    @SerializedName("games_count")
    @Expose
    public String games_count;

    @SerializedName("image_background")
    @Expose
    public String image_background;

    @NonNull
    @Override
    public String toString() {
        return "Id : "+id+"\n"+
                "name : "+name+"\n"+
                "Slug : "+slug+"\n"+
                "language : "+language+"\n"+
                "games_count : "+games_count+"\n"+
                "image_background : "+image_background+"\n";
    }
}

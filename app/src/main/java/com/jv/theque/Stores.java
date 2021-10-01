package com.jv.theque;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stores {

    @SerializedName("store")
    @Expose
    public Store store ;



    @NonNull
    @Override
    public String toString() {
        return store.toString();
    }
}

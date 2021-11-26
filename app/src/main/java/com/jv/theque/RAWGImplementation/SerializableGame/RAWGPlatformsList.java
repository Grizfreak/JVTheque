package com.jv.theque.RAWGImplementation.SerializableGame;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RAWGPlatformsList implements Serializable {

    @SerializedName("platform")
    @Expose
    public RAWGPlatform RAWGPlatform;



    @NonNull
    @Override
    public String toString() {
        return RAWGPlatform.toString();
    }
}

package com.jv.theque.RAWGImplementation;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RAWGPlatformsList {

    @SerializedName("platform")
    @Expose
    public RAWGPlatform RAWGPlatform;



    @NonNull
    @Override
    public String toString() {
        return RAWGPlatform.toString();
    }
}

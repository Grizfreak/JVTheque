package com.jv.theque;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Platforms {

    @SerializedName("platform")
    @Expose
    public Platform platform ;



    @NonNull
    @Override
    public String toString() {
        return platform.toString();
    }
}

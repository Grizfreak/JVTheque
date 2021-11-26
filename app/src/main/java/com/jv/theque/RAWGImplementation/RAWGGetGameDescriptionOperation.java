package com.jv.theque.RAWGImplementation;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jv.theque.GameImplementation.Game;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RAWGGetGameDescriptionOperation extends AsyncTask<String, Void, String> {
    private String apiKey;
    private Game searchedGame;

    public RAWGGetGameDescriptionOperation(String apiKey, Game game) {
        this.apiKey = apiKey;
        this.searchedGame = game;
    }

    @Override
    protected String doInBackground(String... strings) {
        //Url de la requête
        String uri = "https://api.rawg.io/api/games/" + searchedGame.getSlug() + "?key=" + apiKey;
        Log.i("uri", uri);
        try {
            //On tente une connexion sur la requête
            URLConnection request = new URL(uri).openConnection();
            request.connect();
            //On récupère les données de la page en JSON
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) request.getContent());
            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(inputStreamReader); //Convert the input stream to a json element
            JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.

            return rootObj.get("description").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.jv.theque.RAWGImplementation;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jv.theque.GameImplementation.Game;
import com.jv.theque.RAWGImplementation.SerializableGame.RAWGGame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RAWGSearchOperation extends AsyncTask<String, Void, List<Game>> {
    private String apiKey;
    private String searchedText;
    private List<Game> datalist;
    public RAWGSearchOperation(String apiKey, String search,List datalist){
        this.apiKey = apiKey;
        this.searchedText = search;
    }

    @Override
    protected List<Game> doInBackground(String... strings) {
        //Url de la requête
        String uri = "https://api.rawg.io/api/games?key=" + apiKey + "&search=" + searchedText;
        Log.i("uri",uri);
        try {
            //On tente une connexion sur la requête
            URLConnection request = new URL(uri).openConnection();
            request.connect();
            //On récupère les données de la page en JSON
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) request.getContent());
            // Convert to a JSON object to print data
            datalist = new ArrayList<>();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(inputStreamReader); //Convert the input stream to a json element
            JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
            JsonArray jsonArray = rootObj.get("results").getAsJsonArray();
            Gson gson = new GsonBuilder().serializeNulls().create();
            List<RAWGGame> list = new ArrayList<>();
            if (jsonArray != null) {
                int len = jsonArray.size();
                for (int i = 0; i < len; i++) {
                    //System.out.println(jsonArray.get(i));
                    list.add(gson.fromJson(jsonArray.get(i), RAWGGame.class));
                }
            }


            // FIN
            for (RAWGGame x : list) {
//                Log.i("Game", x.toString());
                //Initialisation des valeurs de la liste de données
                datalist.add(new Game(x));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return datalist;
    }

}

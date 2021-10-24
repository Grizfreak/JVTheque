package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.*;
import com.jv.theque.RAWGImplementation.RAWGGame;


public class MainActivity extends AppCompatActivity {
    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    RecyclerView rV;
    Button validatebtn;
    EditText searchedtext;
    List<Game> datalist;
    GameAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cache la barre d'état (en haut)
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);
        //Instanciation d'une liste contenant les jeux d'une recherche
        datalist = new ArrayList<Game>();
        // Attribution des objets xml à leurs équivalents dans la classe Java
        rV = findViewById(R.id.RecyclerView);
        validatebtn = findViewById(R.id.validate);
        searchedtext = findViewById(R.id.search);
        //Création d'un évènement sur le bouton "Rechercher"
        validatebtn.setOnClickListener(v -> {
            //Création d'une opération asynchrone pour permettre l'usage des connexions internet


            // Le get() permet ici d'attendre que l'opération soit terminée pour passer à la suite
            // TODO : Faire ça plus proprement parce que actuellement ça freeze toute la page
            try {
                new SyncOperation().execute("").get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hideSoftKeyboard(rV);
            updateRecycler(datalist);
                }
        );


    }

    @SuppressLint("StaticFieldLeak")
    private class SyncOperation extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            //Url de la requête
            String uri = "https://api.rawg.io/api/games?key=" + apiKey + "&search=" + searchedtext.getText().toString().replaceAll(" ", "+");
            Log.i("uri",uri);
            try {
                //On tente une connexion sur la requête
                URLConnection request = new URL(uri).openConnection();
                request.connect();
                //On récupère les données de la page en JSON
                InputStreamReader inputStreamReader = new InputStreamReader((InputStream) request.getContent());
                // Convert to a JSON object to print data
                datalist = new ArrayList<Game>();
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
                    Log.i("Game", x.toString());
                    //Initialisation des valeurs de la liste de données
                    datalist.add(new Game(x));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("TIME"," ------ ASYNC FINIE ------ ");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.i("TIME"," ------ ASYNC VRAIMENT FINIE ------ ");
        }
    }

    private void updateRecycler(List<Game> datalist) {
        Log.i("TIME"," ------ UPDATE FINIE ------ ");
        adapter = new GameAdapter(datalist);
        rV.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rV.setAdapter(adapter);
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
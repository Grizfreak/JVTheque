package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;


public class MainActivity extends AppCompatActivity {
    public final String apiKey= "6f8484cb4d6146fea90f7bd967dd96aa";
    RecyclerView rV;
    Button validatebtn;
    EditText searchedtext;
    List<Game> datalist;
    GameAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datalist = new ArrayList<Game>();
        rV = findViewById(R.id.RecyclerView);
        validatebtn = findViewById(R.id.validate);
        searchedtext = findViewById(R.id.search);

        validatebtn.setOnClickListener(v -> {
            new SyncOperation().execute("");
            updateRecycler(datalist);
                }
        );



    }
    @SuppressLint("StaticFieldLeak")
    private class SyncOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            //Url de la requête
            String uri = "https://api.rawg.io/api/games?key=" + apiKey + "&search=" + searchedtext.getText().toString().replaceAll(" ", "+");
            System.out.println(uri);
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
                JsonArray jsonArray = rootObj.get("results").getAsJsonArray();
                Gson gson = new GsonBuilder().serializeNulls().create();
                List<Game> list = new ArrayList<>();
                if (jsonArray != null) {
                    int len = jsonArray.size();
                    for (int i=0;i<len;i++){
                        //System.out.println(jsonArray.get(i));
                        /*list.add(gson.fromJson(jsonArray.get(i), Game.class));*/
                        list.add(gson.fromJson(jsonArray.get(i), Game.class));
                    }
                }
                //Initialisation des valeurs de la liste de données
                datalist = list;

                // FIN
                for(Game x : list){
                    System.out.println(x.toString());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return "yes";
        }
    }

    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        rV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rV.setAdapter(adapter);
    }
}
package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jv.theque.RAWGImplementation.RAWGSearchOperation;


public class MainActivity extends AppCompatActivity {
    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    RecyclerView recyclerView;
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
        datalist = new ArrayList<>();
        // Attribution des objets xml à leurs équivalents dans la classe Java
        recyclerView = findViewById(R.id.RecyclerView);
        validatebtn = findViewById(R.id.validate);
        searchedtext = findViewById(R.id.search);
        //Activation clavier
        //Création d'un évènement sur le bouton "Rechercher"
        validatebtn.setOnClickListener(v -> {
            //Création d'une opération asynchrone pour permettre l'usage des connexions internet


            // Le get() permet ici d'attendre que l'opération soit terminée pour passer à la suite
            // TODO : Faire ça plus proprement parce que actuellement ça freeze toute la page
            try {
                datalist = new RAWGSearchOperation(apiKey,searchedtext.getText().toString().replaceAll(" ", "+"),datalist).execute("").get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            hideSoftKeyboard(recyclerView);
            updateRecycler(datalist);
        }
        );
        searchedtext.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i("nique","pressed"+i);
                if(i == EditorInfo.IME_ACTION_DONE){
                    validatebtn.callOnClick();
                }
                return false;
            }
        });
        configureBottomView();

    }



    // Permet de mettre à jour la recyclerView avec les données des jeux récupérées via l'API
    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    // Permet de "Ranger" le clavier virtuel et de le cacher lorsqu'il est visible à l'écran
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Ajoute un listener sur la barre de navigation (en bas)
    private void configureBottomView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> updateMainFragment(item.getItemId()));

    }


    // TODO : Remplacer les Log par des vraies actions lors d'un changement de page (utiliser les fragment)
    private Boolean updateMainFragment(Integer integer){
        switch (integer) {
            case R.id.action_home:
                Log.i("menuaction","HOME");
                break;
            case R.id.action_search:
                Log.i("menuaction","SEARCH");
                break;
            case R.id.action_favorites:
                Log.i("menuaction","FAVORITES");
                break;
            case R.id.action_settings:
                Log.i("menuaction","SETTINGS");
                setContentView(R.layout.fragment);
                break;
        }
        return true;
    }

}
package com.jv.theque.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jv.theque.App;
import com.jv.theque.DisplayGameActivity;
import com.jv.theque.FetchGames;
import com.jv.theque.gameImplementation.Game;
import com.jv.theque.recyclerViewUsages.GameAdapter;
import com.jv.theque.recyclerViewUsages.ItemClickSupport;
import com.jv.theque.R;
import com.jv.theque.rawgImplementation.RAWGSearchOperation;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements FetchGames {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    RecyclerView recyclerView;
    ImageButton validatebtn;
    EditText searchedtext;
    List<Game> datalist;
    GameAdapter adapter;
    Context context;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().getString(ARG_PARAM1);
            getArguments().getString(ARG_PARAM2);
        }

    }

    // Permet de mettre à jour la recyclerView avec les données des jeux récupérées via l'API
    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_search)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Game game = datalist.get(position);
                    Intent intent = new Intent(App.getAppContext(), DisplayGameActivity.class);
                    intent.putExtra("Game",game);
                    startActivity(intent);
                });
    }


    // Permet de "Ranger" le clavier virtuel et de le cacher lorsqu'il est visible à l'écran
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();



        //Instanciation d'une liste contenant les jeux d'une recherche
        datalist = new ArrayList<>();

        // Attribution des objets xml à leurs équivalents dans la classe Java

        recyclerView = view.findViewById(R.id.RecyclerView);
        validatebtn = view.findViewById(R.id.validate);
        searchedtext = view.findViewById(R.id.search);

        this.configureOnClickRecyclerView();

        //Création d'un évènement sur le bouton "Rechercher"
        validatebtn.setOnClickListener(v -> {

                    if (!internetIsConnected()){
                        Toast.makeText(getContext(), "Veuillez vous connecter à internet afin de chercher un nouveau jeu", Toast.LENGTH_SHORT).show();
                    }

                    //Création d'une opération asynchrone pour permettre l'usage des connexions internet

                    // Le get() permet ici d'attendre que l'opération soit terminée pour passer à la suite
                    new RAWGSearchOperation(apiKey,searchedtext.getText().toString().replaceAll(" ", "+"), this).execute("");

                    hideSoftKeyboard(recyclerView);

                }
        );

        searchedtext.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_DONE){
                validatebtn.callOnClick();
            }
            return false;
        });

        return view;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void query(List<Game> gameList) {
        datalist = gameList;
        updateRecycler(datalist);
    }
}

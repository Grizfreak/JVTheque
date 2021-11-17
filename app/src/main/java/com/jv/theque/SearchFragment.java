package com.jv.theque;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.theque.RAWGImplementation.RAWGSearchOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    RecyclerView recyclerView;
    Button validatebtn;
    EditText searchedtext;
    List<Game> datalist;
    GameAdapter adapter;
    private Fragment mFragment;
    Bundle mBundle;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    // Permet de mettre à jour la recyclerView avec les données des jeux récupérées via l'API
    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_search)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                        Toast.makeText(MainActivity.getContext(),datalist.get(position).getName(), Toast.LENGTH_SHORT).show();
                        //TODO faire la vue mais en version belle maintenant que ça fonctionne
                        Game game = datalist.get(position);
                        Intent intent = new Intent(MainActivity.getContext(), DisplayGameActivity.class);
                        intent.putExtra("Game",game);
                        MainActivity.getContext().startActivity(intent);
                    }
                });
    }


    // Permet de "Ranger" le clavier virtuel et de le cacher lorsqu'il est visible à l'écran
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);



        //Instanciation d'une liste contenant les jeux d'une recherche
        datalist = new ArrayList<>();

        // Attribution des objets xml à leurs équivalents dans la classe Java

        recyclerView = view.findViewById(R.id.RecyclerView);
        validatebtn = view.findViewById(R.id.validate);
        searchedtext = view.findViewById(R.id.search);
        this.configureOnClickRecyclerView();

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

        return view;
    }



}

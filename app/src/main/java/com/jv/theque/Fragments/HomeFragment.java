package com.jv.theque.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.theque.DisplayGameActivity;
import com.jv.theque.GameImplementation.Game;
import com.jv.theque.RecyclerViewUsages.GameAdapter;
import com.jv.theque.RecyclerViewUsages.ItemClickSupport;
import com.jv.theque.MainActivity;
import com.jv.theque.R;
import com.jv.theque.TagsImplementation.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    EditText searchedtext;
    List<Game> datalist;
    LinearLayout tagLayout;
    GameAdapter adapter;
    List<Tag> searchedTags;
    List<Game> actuallyDisplayed;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        //TODO affichage dans le menu
        datalist = MainActivity.userData.getUserGameList().getGameList();
        for (Game g : datalist) {
            Log.e("game", g.getName());
        }
    }

    // Permet de mettre à jour la recyclerView avec les données des jeux récupérées via l'API
    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        actuallyDisplayed = datalist;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_home)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : " + position);
                        Toast.makeText(MainActivity.getContext(), actuallyDisplayed.get(position).getName(), Toast.LENGTH_SHORT).show();
                        //TODO faire la vue mais en version belle maintenant que ça fonctionne
                        Game game = actuallyDisplayed.get(position);
                        Intent intent = new Intent(MainActivity.getContext(), DisplayGameActivity.class);
                        intent.putExtra("Game", game);
                        MainActivity.getContext().startActivity(intent);
                        //TODO on return on fragment updateRecyclerView
                    }
                });
    }

    // Permet de "Ranger" le clavier virtuel et de le cacher lorsqu'il est visible à l'écran
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Attribution des objets xml à leurs équivalents dans la classe Java

        recyclerView = view.findViewById(R.id.HomeRecyclerView);
        searchedtext = view.findViewById(R.id.Homesearch);
        tagLayout = view.findViewById(R.id.TagLinearLayout);
        searchedTags = new ArrayList<>();
        actuallyDisplayed = new ArrayList<>();
        updateRecycler(datalist);
        setTagsButtons();
        this.configureOnClickRecyclerView();
        searchedtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("Michtos","ok je check le changement");
                if(charSequence.toString().trim().toLowerCase(Locale.ROOT).equals("")){
                    updateRecycler(datalist);
                } else {
                    ArrayList result = new ArrayList();
                    for (Game game : actuallyDisplayed){
                        if(game.getName().toLowerCase(Locale.ROOT).contains(charSequence.toString().toLowerCase(Locale.ROOT))){
                            result.add(game);
                        }
                    }
                    updateRecycler(result);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchedtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i("nique", "pressed" + i);
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyboard(recyclerView);
                }
                return false;
            }
        });

        return view;
    }

    private void setTagsButtons() {
            AppCompatButton[] btnWord = new AppCompatButton[MainActivity.userData.getUserTagList().getList().size() + 1];
            for (int i = 0; i < btnWord.length - 1; i++) {
                btnWord[i] = new AppCompatButton(getActivity().getApplicationContext());
                /*btnWord[i].setHeight(50);
                btnWord[i].setWidth(50);*/
                btnWord[i].setBackgroundResource(R.drawable.custom_button);
                btnWord[i].setTextSize(10);
                btnWord[i].setTag(i);
                //TODO modify way to get all Tags
                btnWord[i].setText(MainActivity.userData.getUserTagList().getList().get(i).getName());
                btnWord[i].setOnClickListener(btnClicked);
                tagLayout.addView(btnWord[i]);
            }
        }
    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            Tag tag = MainActivity.userData.getUserTagList().find(b.getText().toString());
            if(searchedTags.contains(tag)){
                searchedTags.remove(tag);
                b.setBackgroundResource(R.drawable.custom_button);
            } else {
                searchedTags.add(tag);
                b.setBackgroundResource(android.R.drawable.btn_default);
            }
            if(searchedTags.isEmpty()){
                updateRecycler(datalist);
            } else {
                ArrayList<Game> result = new ArrayList<>();
             for(Tag t : searchedTags){
                 for(Game game : actuallyDisplayed){
                     if(game.getPlatforms().contains(t)){
                         result.add(game);
                     }
                 }
             }
             updateRecycler(result);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.e("attachment", "resumed");
        updateRecycler(datalist);
    }
}
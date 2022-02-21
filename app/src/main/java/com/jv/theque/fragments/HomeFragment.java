package com.jv.theque.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jv.theque.App;
import com.jv.theque.DisplayGameActivity;
import com.jv.theque.favoritesImplementation.FavoriteSearch;
import com.jv.theque.gameImplementation.Game;
import com.jv.theque.recyclerViewUsages.GameAdapter;
import com.jv.theque.recyclerViewUsages.ItemClickSupport;
import com.jv.theque.MainActivity;
import com.jv.theque.R;
import com.jv.theque.tagsImplementation.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;

    RecyclerView recyclerView;
    public EditText searchedtext;
    List<Game> datalist;
    public LinearLayout tagLayout;
    GameAdapter adapter;
    ImageButton favorite;
    public List<Tag> searchedTags;
    public static FavoriteSearch LastSearch;
    List<Game> actuallyDisplayed;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static void setSearch(FavoriteSearch favoriteSearch) {
        LastSearch = favoriteSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().getString(ARG_PARAM1);
            getArguments().getString(ARG_PARAM2);
        }
        datalist = MainActivity.userData.getUserGameList().getGameList();
    }

    // Permet de mettre à jour la recyclerView avec les données des jeux récupérées via l'API
    private void updateRecycler(List<Game> datalist) {
        adapter = new GameAdapter(datalist);
        actuallyDisplayed = datalist;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_home)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Toast.makeText(App.getAppContext(), actuallyDisplayed.get(position).getName(), Toast.LENGTH_SHORT).show();
                    Game game = actuallyDisplayed.get(position);
                    Intent intent = new Intent(App.getAppContext(), DisplayGameActivity.class);
                    intent.putExtra("Game", game);
                    startActivity(intent);
                });
    }

    // Permet de "Ranger" le clavier virtuel et de le cacher lorsqu'il est visible à l'écran
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Attribution des objets xml à leurs équivalents dans la classe Java

        recyclerView = view.findViewById(R.id.HomeRecyclerView);
        searchedtext = view.findViewById(R.id.Homesearch);
        tagLayout = view.findViewById(R.id.TagLinearLayout);
        favorite = view.findViewById(R.id.favorite);
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
                searchForTags();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchedtext.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(recyclerView);
            }
            return false;
        });
        favorite.setOnClickListener(view -> {
            if (searchedtext.getText().toString().trim().equals("") && searchedTags.isEmpty()) {
                Toast.makeText(view.getContext(), "Veuillez entrer un critère de recherche au minimum", Toast.LENGTH_LONG).show();
            } else {
                FavoriteSearch a = new FavoriteSearch((ArrayList<Tag>) searchedTags, searchedtext.getText().toString());
                Toast.makeText(view.getContext(),"Le favori a été ajouté",Toast.LENGTH_SHORT).show();
                MainActivity.userData.getUserFavorites().add(a);
            }
        });

        return view;
    }

    private void setTagsButtons() {
        tagLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        AppCompatButton[] btnWord = new AppCompatButton[MainActivity.userData.getUserTagList().getList().size() + 1];
        for (int i = 0; i < btnWord.length - 1; i++) {
            Tag t = MainActivity.userData.getUserTagList().getList().get(i);
            int defColor = t.getColor();
            btnWord[i] = new AppCompatButton(requireActivity().getApplicationContext());
            btnWord[i].setBackgroundResource(R.drawable.custom_tag);
            GradientDrawable drawable = (GradientDrawable) btnWord[i].getBackground();
            drawable.setStroke(5, defColor);                                // Changement de la taille et la couleur du contour du tag
            btnWord[i].setTextSize(15);
            btnWord[i].setTransformationMethod(null);

            // Vérifie si l'application est en dark mode et adapte la couleur du texte des tags
            int nightModeFlags =  requireContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:       btnWord[i].setTextColor(Color.WHITE);       break;
                case Configuration.UI_MODE_NIGHT_NO:        btnWord[i].setTextColor(Color.BLACK);       break;
            }

            btnWord[i].setPadding(20, 3, 20, 3);
            btnWord[i].setTag(i);
            drawable.setColor(Color.TRANSPARENT);
            btnWord[i].setLayoutParams(params);
            params.setMargins(15, 0, 0, 0);
            if (searchedTags.contains(t)) {

                drawable.setStroke(5, defColor);                            // Changement de la taille et la couleur du contour du tag
                int r=0,g=0,b=0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int color = (int)Long.parseLong(String.format("%06X", (0xFFFFFF & defColor)), 16);
                    r = (color >> 16) & 0xFF;
                    g = (color >> 8) & 0xFF;
                    b = (color) & 0xFF;
                }
                drawable.setColor(Color.argb(50, r, g, b));   // Changement de la couleur d'arrière-plan du tag
                btnWord[i].setTextSize(15);
                btnWord[i].setPadding(20, 3, 20, 3);
            }
            btnWord[i].setText(t.getName());
            btnWord[i].setOnClickListener(btnClicked);
            tagLayout.addView(btnWord[i]);
        }
    }

    final View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button btn = (Button) v;
            GradientDrawable drawable = (GradientDrawable) btn.getBackground();
            Tag tag = MainActivity.userData.getUserTagList().find(btn.getText().toString());
            int defColor = tag.getColor();
            if (searchedTags.contains(tag)) {
                searchedTags.remove(tag);
                drawable.setStroke(5, defColor); // set stroke width and stroke color
                drawable.setColor(Color.TRANSPARENT);           // set solid color
            } else {
                searchedTags.add(tag);
                drawable.setStroke(5, defColor); // set stroke width and stroke color
                int r=0,g=0,b=0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int color = (int)Long.parseLong(String.format("%06X", (0xFFFFFF & defColor)), 16);
                    r = (color >> 16) & 0xFF;
                    g = (color >> 8) & 0xFF;
                    b = (color) & 0xFF;
                }
                drawable.setColor(Color.argb(50, r, g, b));   // Changement de la couleur d'arrière-plan du tag
            }
            btn.setTextSize(15);
            searchForTags();
        }
    };

    private void searchForTags() {
        if (searchedTags.isEmpty() && searchedtext.getText().toString().trim().toLowerCase(Locale.ROOT).equals("")) {
            updateRecycler(datalist);
        } else {
            ArrayList<Game> result = new ArrayList<>();
            if (searchedtext.getText().toString().trim().toLowerCase(Locale.ROOT).equals("")) {
                for (Tag t : searchedTags) {
                    for (Game game : datalist) {
                        if (game.getTags().contains(t)) {
                            if (!result.contains(game)) {
                                result.add(game);
                            }
                        }
                    }
                }
            } else if (searchedTags.isEmpty()) {
                for (Game game : datalist) {
                    if (game.getName().toLowerCase(Locale.ROOT).contains(searchedtext.getText().toString().toLowerCase(Locale.ROOT)) && !result.contains(game)) {
                        result.add(game);
                    }
                }
            } else {
                for (Tag t : searchedTags) {
                    for (Game game : datalist) {
                        if (!result.contains(game) && game.getTags().contains(t) && game.getName().toLowerCase(Locale.ROOT).contains(searchedtext.getText().toString().toLowerCase(Locale.ROOT))) {
                            result.add(game);
                        }
                    }
                }
            }
            updateRecycler(result);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecycler(datalist);
        setTagsButtons();
        if (LastSearch != null) {
            searchedtext.setText(LastSearch.getText_searched());
            searchedTags = (List<Tag>) LastSearch.getTag_searched().clone();
            setTagsButtons();
            searchForTags();
            LastSearch = null;
        }
    }
}
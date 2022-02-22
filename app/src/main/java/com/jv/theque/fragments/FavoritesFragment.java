package com.jv.theque.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jv.theque.favoritesImplementation.FavoriteSearch;
import com.jv.theque.MainActivity;
import com.jv.theque.R;
import com.jv.theque.tagsImplementation.Tag;

import java.util.Objects;

public class FavoritesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView displayList;

    public FavoritesFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        displayList = view.findViewById(R.id.displayList);
        displayList.setOnItemClickListener((adapterView, view12, i, l) -> {
            FavoriteSearch a = MainActivity.userData.getUserFavorites().getList().get(i);
            a.changeFragment((AppCompatActivity) view12.getContext());
        });
        displayList.setOnItemClickListener((parent, view1, position, id) -> createValidationPopUp(view1, position));
        ArrayAdapter<FavoriteSearch> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, MainActivity.userData.getUserFavorites().getList());
        displayList.setAdapter(arrayAdapter);
        return view;
    }

    private void createValidationPopUp(View view1, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(view1).getContext());

        // Titre de l'alerte et message
        builder.setTitle("Confirmation").setMessage("Voulez-vous retirer ce favori ?");

        // Annulation et icône
        builder.setCancelable(true);
        builder.setIcon(R.drawable.logo);

        // Création d'un bouton "Oui" avec un OnClickListener
        builder.setPositiveButton("Oui", (dialog, id) -> {
            MainActivity.userData.getUserFavorites().getList().remove(position);
            ArrayAdapter<FavoriteSearch> arrayAdapter = new ArrayAdapter<>(view1.getContext(), android.R.layout.simple_list_item_1, MainActivity.userData.getUserFavorites().getList());
            displayList.setAdapter(arrayAdapter);
            Toast.makeText(view1.getContext(),"Le favori a été supprimé",Toast.LENGTH_SHORT).show();
        });

        // Création d'un bouton "Non" avec un OnClickListener
        builder.setNegativeButton("Non", (dialog, id) -> {
            // Retour
            dialog.cancel();
        });

        // Création d'un AlertDialog
        AlertDialog alert = builder.create();
        alert.show();
    }

}
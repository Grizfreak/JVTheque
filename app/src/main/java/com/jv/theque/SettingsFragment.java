package com.jv.theque;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.theque.RAWGImplementation.RAWGSearchOperation;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Variables
    private RadioGroup radioGroupTheme;

    private Button exportButton;
    private Button clearButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        radioGroupTheme = view.findViewById(R.id.radioGroupTheme);

        // TODO : Définir par défault le choix du thème
        //  Au premier lancement : Thème 'système' par défaut
        //  Aux lancements suivants : On définit le thème au démarrage de l'appli suivant le choix enregistré de l'utilisateur (persistance)

        // Ajout d'un listener sur le RadioGroup correspondant au choix du thème de l'app
        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Définit le thème de l'application suivant le RadioButton sélectionné
                switch (checkedId) {
                    case R.id.radioButtonLight:
                        Log.i("THEME", "NIGHT MODE OFF");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.radioButtonDark:
                        Log.i("THEME", "NIGHT MODE ON");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    case R.id.radioButtonSystem:
                        Log.i("THEME", "NIGHT MODE SYSTEM");
                        Toast.makeText(getContext(), "Thème défini en fonction du système", Toast.LENGTH_SHORT).show();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                }
            }
        });
        exportButton = view.findViewById(R.id.exportList);
        clearButton = view.findViewById(R.id.clearList);


        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.UserGameList.saveToFile();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.UserGameList.clear();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}
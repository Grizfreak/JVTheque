package com.jv.theque;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

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

    private UiModeManager uiModeManager;
    private Switch nighModeSwitch;
    private Button systemColorBtn;

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

        uiModeManager = (UiModeManager) getContext().getSystemService(Context.UI_MODE_SERVICE);

        nighModeSwitch = view.findViewById(R.id.nighModeSwitch);
        systemColorBtn = view.findViewById(R.id.systemColorBtn);

        // Change le thème de l'application en fonction de la position du switch du mode sombre
        nighModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i("NIGHT", "NIGHT MODE ON");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                Log.i("NIGHT", "NIGHT MODE OFF");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Change le thème de l'application en fonction du thème du système, lors d'un appui long sur le switch du mode sombre
        nighModeSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("NIGHT", "NIGHT MODE SYSTEM");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Toast.makeText(getContext(), "Mode défini en fonction du système", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Fait tout pareil que juste au-dessus mais en bleu. Non en fait c'est pareil mais en appuyant sur le bouton 'COULEUR DU SYSTEME', je vais sans doute l'enlever à voir
        systemColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("NIGHT", "NIGHT MODE SYSTEM");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                Toast.makeText(getContext(), "Mode défini en fonction du système", Toast.LENGTH_LONG).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
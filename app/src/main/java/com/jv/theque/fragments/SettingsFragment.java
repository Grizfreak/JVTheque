package com.jv.theque.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.theque.MainActivity;
import com.jv.theque.R;

public class SettingsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView info_text, info_sign,cgu_text,cgu_sign,ml_text,ml_sign;

    public SettingsFragment() {
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        // Variables
        RadioGroup radioGroupTheme = view.findViewById(R.id.radioGroupTheme);
        RadioButton radioButtonLight = view.findViewById(R.id.radioButtonLight);
        RadioButton radioButtonDark = view.findViewById(R.id.radioButtonDark);
        RadioButton radioButtonSystem = view.findViewById(R.id.radioButtonSystem);

        Button button_cgu = view.findViewById(R.id.button_cgu);
        Button button_info = view.findViewById(R.id.button_info);
        Button button_ml = view.findViewById(R.id.button_ml);
        info_sign = view.findViewById(R.id.information_sign);
        info_text = view.findViewById(R.id.info_text);
        ml_sign = view.findViewById(R.id.ml_sign);
        ml_text = view.findViewById(R.id.ml_text);
        cgu_sign = view.findViewById(R.id.cgu_sign);
        cgu_text = view.findViewById(R.id.cgu_text);

        ml_text.setVisibility(View.GONE);
        cgu_text.setVisibility(View.GONE);
        info_text.setVisibility(View.GONE);


        int night_mode = MainActivity.userData.getThemeMode();
        switch (night_mode){
            case AppCompatDelegate.MODE_NIGHT_NO:
                radioButtonLight.toggle();
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                radioButtonDark.toggle();
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                radioButtonSystem.toggle();
                break;
        }

        // Ajout d'un listener sur le RadioGroup correspondant au choix du thème de l'app
        radioGroupTheme.setOnCheckedChangeListener(this::onCheckedChanged);

        Button exportButton = view.findViewById(R.id.exportList);
        Button clearButton = view.findViewById(R.id.clearList);


        exportButton.setOnClickListener(v -> MainActivity.userData.saveToFile());

        clearButton.setOnClickListener(v -> {
            MainActivity.userData.getUserGameList().getList().clear();
            MainActivity.userData.getUserTagList().getList().clear();
        });

        button_info.setOnClickListener(onAproposClicked);
        button_ml.setOnClickListener(onAproposClicked);
        button_cgu.setOnClickListener(onAproposClicked);


        // Inflate the layout for this fragment
        return view;
    }

    final View.OnClickListener onAproposClicked = new View.OnClickListener(){
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            TextView sign, text;
            switch (b.getId()){
                case R.id.button_cgu:
                        sign = cgu_sign;
                        text = cgu_text;
                    break;
                case R.id.button_ml:
                        sign = ml_sign;
                        text = ml_text;
                    break;
                case R.id.button_info:
                        sign = info_sign;
                        text = info_text;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + b.getId());
            }
            if (text.getVisibility() == View.VISIBLE){
                text.setVisibility(View.GONE);
                sign.setText(">");
            } else {
                text.setVisibility(View.VISIBLE);
                sign.setText("v");
            }
        }
    };

    @SuppressLint("NonConstantResourceId")
    private void onCheckedChanged(RadioGroup group, int checkedId) {
        int mode = 0;
        // Définit le thème de l'application suivant le RadioButton sélectionné
        switch (checkedId) {
            case R.id.radioButtonLight:
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case R.id.radioButtonDark:
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case R.id.radioButtonSystem:
                Toast.makeText(getContext(), "Thème défini en fonction du système", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        if (mode == 0){
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
        MainActivity.userData.setThemeMode(mode);
    }
}
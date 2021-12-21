package com.jv.theque.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jv.theque.CustomDialog;
import com.jv.theque.MainActivity;
import com.jv.theque.R;
import com.jv.theque.TagsImplementation.Tag;

import java.util.ArrayList;
import java.util.List;

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
    private Button showTags;

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
        showTags = view.findViewById(R.id.showTags);

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

        showTags.setOnClickListener(v -> {
            List<Tag> test = new ArrayList<>();
            CustomDialog.showAlertDialogTag(v.getContext(),test);
            /*AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            // Set Title.
            builder.setTitle("See Tags available");

            // Add a list
            ArrayList<String> tagNames = new ArrayList<>();
            for (Tag tag : MainActivity.userData.getUserTagList().getList()){
                tagNames.add(tag.getName());
            }
            tagNames.add("+");
            builder.setItems(tagNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Tag tag = null;
                    if(which == MainActivity.userData.getUserTagList().getList().size()){
                        Toast.makeText(v.getContext(),"yes t'a touché le plus",Toast.LENGTH_SHORT).show();
                    } else {
                        tag = MainActivity.userData.getUserTagList().getList().get(which);
                    }
                    dialog.dismiss(); // Close Dialog
                    // Do some thing....
                    // For example: Call method of MainActivity.
                    if(tag != null){
                        Toast.makeText(v.getContext(),"You select: " + tag.getName(),
                                Toast.LENGTH_SHORT).show();
                    }

                    // activity.someMethod(animal);
                }
            });

            //
            builder.setCancelable(true);

            // Create "Cancel" button with OnClickListener.
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(v.getContext(),"You choose No button",
                            Toast.LENGTH_SHORT).show();
                    //  Cancel
                    dialog.cancel();
                }
            });

            // Create AlertDialog:
            AlertDialog alert = builder.create();
            alert.show();*/
        });
        exportButton = view.findViewById(R.id.exportList);
        clearButton = view.findViewById(R.id.clearList);


        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.userData.saveToFile();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.userData.getUserGameList().getList().clear();
                MainActivity.userData.getUserTagList().getList().clear();
                return;
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}
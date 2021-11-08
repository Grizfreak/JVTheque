package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jv.theque.RAWGImplementation.RAWGSearchOperation;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static Context context;
    //TODO Persistance de cette magnifique liste
    public static List<Game> UserGameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Cache la barre d'état (en haut)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        this.context = this;
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        // Initialise le NavHostFragment, qui va gérer la navigation dans les View avec les Fragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            // Initialise la NavigationUI avec la BottomNavigationView
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

    }
    public static Context getContext(){
        return context;
    }
}
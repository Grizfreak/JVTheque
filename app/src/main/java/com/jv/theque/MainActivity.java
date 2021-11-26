package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.content.Context;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jv.theque.GameImplementation.UserGameList;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static Context context;
    //TODO Persistance de cette magnifique liste
    public static com.jv.theque.GameImplementation.UserGameList UserGameList = new UserGameList();



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
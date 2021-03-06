package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public static final UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Cache la barre d'état (en haut)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        // Initialise le NavHostFragment, qui va gérer la navigation dans les View avec les Fragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            // Initialise la NavigationUI avec la BottomNavigationView
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Si la liste de jeux de l'utilisateur est vide (premier lancement par exemple),
            // on affiche la page de recherche d'un jeu plutôt que la page d'accueil
            if (MainActivity.userData.getUserGameList().getGameList().isEmpty() && App.isFirstLoad()) {
                bottomNavigationView.setSelectedItemId(R.id.searchFragment);
                App.setFirstLoad(false);
            }
        }

    }
}
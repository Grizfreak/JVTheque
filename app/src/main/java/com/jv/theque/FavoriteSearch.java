package com.jv.theque;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.jv.theque.Fragments.HomeFragment;
import com.jv.theque.TagsImplementation.Tag;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSearch {
    private ArrayList<Tag> searched;
    private String text_searched;

    public FavoriteSearch(ArrayList<Tag> searched,String text_searched){
        this.searched=searched;
        this.text_searched=text_searched;
    }

    public String getText_searched() {
        return text_searched;
    }

    public ArrayList<Tag> getSearched() {
        return searched;
    }

    public void changeFragment(AppCompatActivity activity){
        NavHostFragment n =(NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = n.getNavController();
        navController.navigateUp();
        navController.navigate(R.id.action_searchFragment_to_homeFragment);
        HomeFragment.setSearch(this);

    }

    @NonNull
    @Override
    public String toString() {
        return this.text_searched + " / ";
    }
}

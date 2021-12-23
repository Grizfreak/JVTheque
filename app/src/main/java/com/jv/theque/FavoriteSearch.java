package com.jv.theque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.jv.theque.Fragments.HomeFragment;
import com.jv.theque.TagsImplementation.Tag;

import java.io.Serializable;
import java.util.ArrayList;

public class FavoriteSearch implements Serializable {
    private ArrayList<Tag> tag_searched;
    private String text_searched;

    public FavoriteSearch(ArrayList<Tag> tag_searched, String text_searched) {
        this.tag_searched = tag_searched;
        this.text_searched = text_searched;
    }

    public String getText_searched() {
        return text_searched;
    }

    public ArrayList<Tag> getTag_searched() {
        return tag_searched;
    }

    public void changeFragment(AppCompatActivity activity) {
        NavHostFragment n = (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = n.getNavController();
        navController.navigateUp();
        navController.navigate(R.id.action_searchFragment_to_homeFragment);
        HomeFragment.setSearch(this);

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        if (text_searched.length() > 0) {
            strBuilder.append('"');
            strBuilder.append(text_searched);
            strBuilder.append('"');
        }

        if (tag_searched.size() > 0) {
            strBuilder.append(" Tags : ");

            for (int i = 0; i < tag_searched.size(); i++) {
                Tag tag = tag_searched.get(i);
                strBuilder.append(tag.getName());
                if (i < tag_searched.size() - 1) {
                    strBuilder.append(", ");
                }

            }
        }


        return strBuilder.toString();
    }
}

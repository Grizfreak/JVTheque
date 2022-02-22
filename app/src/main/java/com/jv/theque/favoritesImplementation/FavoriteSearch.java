package com.jv.theque.favoritesImplementation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.jv.theque.R;
import com.jv.theque.fragments.HomeFragment;
import com.jv.theque.tagsImplementation.Tag;

import java.io.Serializable;
import java.util.ArrayList;

public class FavoriteSearch implements Serializable {
    private final ArrayList<Tag> tag_searched;
    private final String text_searched;

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
        assert n != null;
        NavController navController = n.getNavController();
        navController.navigateUp();
        navController.navigate(R.id.action_homeFragment_to_homeFragment);
        HomeFragment.setSearch(this);

    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        if (text_searched.length() > 0) {
            strBuilder.append("Recherche : ");
            strBuilder.append('"');
            strBuilder.append(text_searched);
            strBuilder.append("\"\n");
        }

        if (tag_searched.size() > 0) {
            strBuilder.append("Tags : ");

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

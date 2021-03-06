package com.jv.theque;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jv.theque.gameImplementation.Game;
import com.jv.theque.tagsImplementation.RAWGTag;
import com.jv.theque.tagsImplementation.Tag;
import com.jv.theque.tagsImplementation.UserTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomDialog {


    private static FloatingActionButton prevSelectColor = null;
    // Permet de sélectionner le bouton de choix de la couleur (et le stocker dans une variable)
    static final View.OnClickListener colorBtnListener = view -> {
        FloatingActionButton b = (FloatingActionButton) view;
        if (prevSelectColor != null)  prevSelectColor.setImageResource(0);
        b.setImageResource(R.drawable.check);
        prevSelectColor = b;
    };

    public static void showAlertDialogTag(final DisplayGameActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle("Ajouter un tag");

        // Add a list
        String[] tags = MainActivity.userData.getUserTagList().getTagNameList().toArray(new String[0]);
        builder.setItems(tags, (dialogInterface, i) -> {
          String tag = tags[i];
              dialogInterface.dismiss();
              Tag tagToAdd = MainActivity.userData.getUserTagList().find(tag);
              activity.gameDisplayed.addTag(tagToAdd);
              activity.updateTags();
        });


        builder.setNeutralButton("Ajouter un nouveau tag", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.add_new_tag_dialog, null);
            alert.setView(dialogView);
            EditText txt = dialogView.findViewById(R.id.editTextTextPersonName);

            LinearLayout ll = dialogView.findViewById(R.id.btnLinearLayout);
            final int childCount = ll.getChildCount();
            for (int k = 0; k < childCount; k++) {
                FloatingActionButton v = (FloatingActionButton) ll.getChildAt(k);
                v.setOnClickListener(colorBtnListener);
            }


            alert.setPositiveButton("Valider", (dialogInterface12, i12) -> {
                if (txt.getText().toString().trim().length() == 0) {
                    return;
                }
                UserTag us = new UserTag(txt.getText().toString());
                int color;
                int id = prevSelectColor.getId();
                if      (id == R.id.fab1) color = activity.getResources().getColor(R.color.RED, activity.getTheme());
                else if (id == R.id.fab2) color = activity.getResources().getColor(R.color.GREEN_DARK, activity.getTheme());
                else if (id == R.id.fab3) color = activity.getResources().getColor(R.color.BLUE_DARK, activity.getTheme());
                else if (id == R.id.fab4) color = activity.getResources().getColor(R.color.BEIGE, activity.getTheme());
                else if (id == R.id.fab5) color = activity.getResources().getColor(R.color.PINK, activity.getTheme());
                else if (id == R.id.fab6) color = activity.getResources().getColor(R.color.PURPLE, activity.getTheme());
                else if (id == R.id.fab7) color = activity.getResources().getColor(R.color.YELLOW, activity.getTheme());
                else if (id == R.id.fab8) color = activity.getResources().getColor(R.color.BLUE_LIGHT, activity.getTheme());
                else if (id == R.id.fab9) color = activity.getResources().getColor(R.color.GREEN_LIGHT, activity.getTheme());
                else if (id == R.id.fab10) color = activity.getResources().getColor(R.color.ORANGE, activity.getTheme());
                else  color = Color.BLACK;

                us.setColor(color);
                MainActivity.userData.getUserTagList().add(us);
//                    gameDisplayed.addUserTagtoList(us);
                activity.gameDisplayed.addTag(us);
                activity.updateTags();
            });
            alert.setNegativeButton("Annuler", (dialogInterface1, i1) -> {

            });
            alert.show();
        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDialogNewGameAdd(final DisplayGameActivity activity, List<Tag> result, Game toDisplay) {
        //Création d'un dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle("Choisissez les plateformes");

        // Ajout de la liste à afficher
        ArrayList<String> toDisplayInString = new ArrayList<>();
        // Création d'un tableau de strings pour le format du builder
        for (Tag tag : toDisplay.getPlatforms()) {
            toDisplayInString.add(tag.getName());
        }
        final String[] tags = toDisplayInString.toArray(new String[0]);
        // Création d'un tableau référençant les objets validés ou non
        final boolean[] checkedInfos = new boolean[tags.length];
        Arrays.fill(checkedInfos, false);
        //Application d'un mode multichoix sur une liste de Strings
        builder.setMultiChoiceItems(tags, checkedInfos, (dialog, which, isChecked) -> {
            //A chaque appui sur un item, passage de son état booléen
            checkedInfos[which] = isChecked;
        });

        //
        builder.setCancelable(true);
        builder.setIcon(R.drawable.logo);

        // Création du bouton pour valider le dialogue
        builder.setPositiveButton("OK", (dialog, id) -> {
            //Vérification de tous les objets true du tableau booléen
            for (int i = 0; i < checkedInfos.length; i++) {
                boolean e = checkedInfos[i];
                if (e) {
                    //Si vrai alors ajout du tag dans les résultats
                    result.add(new RAWGTag(tags[i], toDisplay));
                }
            }
            // Close Dialog
            dialog.dismiss();
            //Après la fermeture du dialogue vérification du résultat utilisateur
            activity.checkInputUser(result);
        });

        // Create "Cancel" button with OnClickListener.
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            //  Cancel
            dialog.cancel();
        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }
}

package com.jv.theque;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.jv.theque.GameImplementation.Game;
import com.jv.theque.TagsImplementation.RAWGTag;
import com.jv.theque.TagsImplementation.Tag;
import com.jv.theque.TagsImplementation.UserTagList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomDialog {

    public static void showAlertDialogTag(final Context activity, List<Tag> result)  {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            // Set Title.
            builder.setTitle("Select");

            // Add a list
            final String[] tags = MainActivity.userData.getUserTagList().getTagNameList().toArray(new String[0]);

            final boolean[] checkedInfos = new boolean[tags.length];
            Arrays.fill(checkedInfos,false);
            //TODO if game has tags


            builder.setMultiChoiceItems(tags, checkedInfos, new DialogInterface.OnMultiChoiceClickListener()  {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedInfos[which] = isChecked;
                }
            });

            //
            builder.setCancelable(true);
            builder.setIcon(R.drawable.logo);

            // Create "Yes" button with OnClickListener.
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Close Dialog
                    dialog.dismiss();
                    for (int i = 0;i < checkedInfos.length;i++){
                        boolean e = checkedInfos[i];
                        if (e == true){
                            result.add(MainActivity.userData.getUserTagList().find(tags[i]));
                        }
                    }
                    for(Tag tag : result){
                        Log.e("Michtos",tag.getName());
                    }
                }
            });

            // Create "Cancel" button with OnClickListener.
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(activity,"You choose Cancel button",
                            Toast.LENGTH_SHORT).show();
                    //  Cancel
                    dialog.cancel();
                }
            });

            // Create AlertDialog:
            AlertDialog alert = builder.create();
            alert.show();
        }

    public static void showDialogNewGameAdd(final DisplayGameActivity activity, List<Tag> result, Game toDisplay)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set Title.
        builder.setTitle("Select");

        // Add a list
        ArrayList<String> toDisplayInString = new ArrayList<>();
        for (Tag tag : toDisplay.getPlatforms()){
            toDisplayInString.add(tag.getName());
        }
        final String[] tags = toDisplayInString.toArray(new String[0]);

        final boolean[] checkedInfos = new boolean[tags.length];
        Arrays.fill(checkedInfos,false);
        //TODO if game has tags


        builder.setMultiChoiceItems(tags, checkedInfos, new DialogInterface.OnMultiChoiceClickListener()  {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedInfos[which] = isChecked;
            }
        });

        //
        builder.setCancelable(true);
        builder.setIcon(R.drawable.logo);

        // Create "Yes" button with OnClickListener.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i = 0;i < checkedInfos.length;i++){
                    boolean e = checkedInfos[i];
                    if (e == true){
                        result.add(new RAWGTag(tags[i],toDisplay));
                    }
                }
                for(Tag tag : result){
                    Log.e("Michtos",tag.getName());
                }

                // Close Dialog
                dialog.dismiss();
                activity.checkInputUser(result);
            }
        });

        // Create "Cancel" button with OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(activity,"You choose Cancel button",
                        Toast.LENGTH_SHORT).show();
                //  Cancel
                dialog.cancel();
                notifyAll();
            }

        });
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }
}

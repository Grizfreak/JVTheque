package com.jv.theque;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jv.theque.GameImplementation.Game;
import com.jv.theque.GameImplementation.UserGameList;
import com.jv.theque.RAWGImplementation.DownloadImageTask;
import com.jv.theque.RAWGImplementation.RAWGGetGameDescriptionOperation;
import com.jv.theque.TagsImplementation.Tag;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DisplayGameActivity extends AppCompatActivity {

    private ImageButton returnButton;
    private TextView title, releaseDate;
    private ImageView gameImage;
    private Game gameDisplayed;
    private Intent intent;
    private Button removeButton;
    private FloatingActionButton addButton;
    private LinearLayout platformLayout, userTagLayout;
    private TextView description;
    View.OnClickListener btnaddClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Toast.makeText(getApplicationContext(), "clicked add button", Toast.LENGTH_SHORT).show();
            List<Tag> result = new ArrayList<Tag>();
            CustomDialog.showAlertDialogTag(v.getContext(), result);
        }
    };
    UserGameList userGameList;
    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    private boolean inList = false;

    protected void checkInputUser(List<Tag> result) {
        //Vérification du choix de l'utilisateur
        if (result.size() == 0) {
            //Si celui ci est incorrect, affichage d'un message
            Toast.makeText(this, "frérot met un tag au moins", Toast.LENGTH_SHORT).show();
        } else {
            //Si celui ci est valide, modification des tags, et ajout du jeu en local
            gameDisplayed.setRAWGTags(result);
            userGameList.addGame(gameDisplayed);
            gameDisplayed.addTagsToList();
            onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        intent = getIntent();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        addButton = findViewById(R.id.addButton);
        title = findViewById(R.id.GameTitle);
        gameImage = findViewById(R.id.GameImage);
        removeButton = findViewById(R.id.removeButton);
        releaseDate = findViewById(R.id.releaseDate);
        platformLayout = findViewById(R.id.PlatformLayout);
        description = findViewById(R.id.gameDescription);
        userTagLayout = findViewById(R.id.UserTagLayout);
        Bundle extras = getIntent().getExtras();
        gameDisplayed = (Game) intent.getSerializableExtra("Game");
        if (MainActivity.userData.getUserGameList().contains(gameDisplayed)) {
            inList = true;
        }
        title.setText(gameDisplayed.getName());
        releaseDate.setText((gameDisplayed.getRelease_date().equals(Game.DEFAULT_DATE)) ?
                (releaseDate.getText() + " inconnue") : (releaseDate.getText() + " " + formatter.format(gameDisplayed.getRelease_date())));
        setPlatformButtons(gameDisplayed.getPlatforms().size());
        displayImage(gameDisplayed, gameImage);

        //TODO modifier pour gérer la suppression d'un jeu

        userGameList = MainActivity.userData.getUserGameList();

        if (gameDisplayed.getDescription() == null) {
            try {
                String newGameDescription = new RAWGGetGameDescriptionOperation(apiKey, gameDisplayed).execute("").get();
                Log.i("GameDescription", newGameDescription);
                newGameDescription = newGameDescription.replaceAll("<p>", "").replaceAll("</p>", "\n").replaceAll("<br />","\n");
                gameDisplayed.setDescription(newGameDescription);
                description.setText(description.getText() + "\n" + gameDisplayed.getDescription());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            description.setText(description.getText() + "\n" + gameDisplayed.getDescription());
        }


        if (inList) {
            addButton.setVisibility(View.GONE);
        } else {
            removeButton.setVisibility(View.GONE);
        }
        if(inList) {
            AppCompatButton btnAddTag = new AppCompatButton(this);
            btnAddTag.setText("+");
            btnAddTag.setOnClickListener(addANewTag);
            userTagLayout.addView(btnAddTag);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instanciation d'une liste de résultats du dialogue
                List<Tag> result = new ArrayList<>();
                //Affichage du dialogue d'ajout des premiers tags d'un jeu
                CustomDialog.showDialogNewGameAdd(getActivity(), result, gameDisplayed);
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userGameList.removeGame(gameDisplayed);
                onBackPressed();
            }
        });

    }

    View.OnClickListener addANewTag = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            newTagDialog cdd = new newTagDialog(DisplayGameActivity.this);
            cdd.show();
        }
    };

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
        }
    };

    private void setPlatformButtons(int size) {
        AppCompatButton[] btnWord = new AppCompatButton[size + 1];
        for (int i = 0; i < btnWord.length - 1; i++) {
            btnWord[i] = new AppCompatButton(this);
            btnWord[i].setHeight(10);
            btnWord[i].setWidth(10);
            btnWord[i].setBackgroundResource(R.drawable.custom_button);
            btnWord[i].setTextSize(10);
            btnWord[i].setTag(i);
            btnWord[i].setText(gameDisplayed.getPlatforms().get(i).toString());
            btnWord[i].setOnClickListener(btnClicked);
            platformLayout.addView(btnWord[i]);
        }
    }

    void displayTags(int size) {
        AppCompatButton[] btnWord = new AppCompatButton[size + 1];
        for (int i = 0; i < btnWord.length - 1; i++) {
            btnWord[i] = new AppCompatButton(this);
            btnWord[i].setHeight(10);
            btnWord[i].setWidth(10);
            btnWord[i].setBackgroundResource(R.drawable.custom_button);
            btnWord[i].setTextSize(10);
            btnWord[i].setTag(i);
            btnWord[i].setText(gameDisplayed.getPlatforms().get(i).toString());
            btnWord[i].setOnClickListener(btnClicked);
            platformLayout.addView(btnWord[i]);
        }
        Log.e("Michto", "J'suis rentré");
        btnWord[size] = new AppCompatButton(this);
        btnWord[size].setHeight(10);
        btnWord[size].setWidth(10);
        btnWord[size].setBackgroundResource(R.drawable.custom_button);
        btnWord[size].setTextSize(10);
        btnWord[size].setTag(size);
        btnWord[size].setText("+");
        btnWord[size].setOnClickListener(btnaddClicked);
        platformLayout.addView(btnWord[size]);
    }

    void displayImage(Game game, ImageView gamePicture) {
        if (game.getBackgroundImageLink() != null) {

            if (new File(MainActivity.getContext().getApplicationContext().getCacheDir(), game.getSlug() + ".png").exists()) {
                File gamePicFile = new File(MainActivity.getContext().getApplicationContext().getCacheDir(), game.getSlug() + ".png");
                Bitmap bitmap = BitmapFactory.decodeFile(gamePicFile.getAbsolutePath());
                gamePicture.setImageBitmap(bitmap);
            } else {

                new DownloadImageTask(gamePicture, game.getSlug())
                        .execute(game.getBackgroundImageLink().replace("https://media.rawg.io/media/games/", "https://api.rawg.io/media/resize/420/-/games/"));
//                    Log.i("INFO", game.backgroundImageLink);
            }
        }
    }

    private DisplayGameActivity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (DisplayGameActivity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
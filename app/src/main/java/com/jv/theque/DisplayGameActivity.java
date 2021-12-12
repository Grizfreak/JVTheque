package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
    private Button addButton, removeButton;
    private LinearLayout platformLayout;
    private TextView description;
    UserGameList userGameList;
    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";

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
        Bundle extras = getIntent().getExtras();
        gameDisplayed = (Game) intent.getSerializableExtra("Game");
        title.setText(gameDisplayed.getName());
        Log.e("default date compare", String.valueOf(gameDisplayed.getRelease_date()) + " / " + String.valueOf(Game.DEFAULT_DATE));
        // Affiche la date correspondante du jeu, si la date n'est pas connue (valeur par défaut), affiche "Date de sortie : inconnue"
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
                newGameDescription = newGameDescription.replaceAll("<p>", "").replaceAll("</p>", "\n");
                gameDisplayed.setDescription(newGameDescription);
                description.setText(description.getText() + "\n" + gameDisplayed.getDescription());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            description.setText(description.getText() + "\n" + gameDisplayed.getDescription());
        }


        if (userGameList.contains(gameDisplayed)) {
            addButton.setVisibility(View.GONE);
        } else {
            removeButton.setVisibility(View.GONE);
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

    private void setPlatformButtons(int size) {
        AppCompatButton[] btnWord = new AppCompatButton[size];
        for (int i = 0; i < btnWord.length; i++) {
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

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
        }
    };

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
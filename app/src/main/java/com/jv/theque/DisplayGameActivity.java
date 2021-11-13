package com.jv.theque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class DisplayGameActivity extends AppCompatActivity {

    private ImageButton returnButton;
    private TextView title;
    private ImageView gameImage;
    private Game gameDisplayed;
    private Intent intent;
    private Button addButton, removeButton;

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
        addButton = findViewById(R.id.addButton);
        title = findViewById(R.id.GameTitle);
        gameImage = findViewById(R.id.GameImage);
        removeButton = findViewById(R.id.removeButton);
        Bundle extras = getIntent().getExtras();
        gameDisplayed = (Game) intent.getSerializableExtra("Game");
        title.setText(gameDisplayed.getName());
        displayImage(gameDisplayed,gameImage);
        //TODO modifier pour gérer la suppression d'un jeu
        if(MainActivity.UserGameList.contains(gameDisplayed)){
            addButton.setVisibility(View.GONE);
        }else{
            removeButton.setVisibility(View.GONE);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.UserGameList.addGame(gameDisplayed);
                onBackPressed();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                MainActivity.UserGameList.removeGame(gameDisplayed);
                onBackPressed();
            }
        });

    }
    void displayImage(Game game, ImageView gamePicture) {
        if (game.getBackgroundImageLink() != null) {

            if (new File(App.getAppContext().getCacheDir(), game.getSlug() + ".png").exists()) {
                File gamePicFile = new File(App.getAppContext().getCacheDir(), game.getSlug() + ".png");
                Bitmap bitmap = BitmapFactory.decodeFile(gamePicFile.getAbsolutePath());
                gamePicture.setImageBitmap(bitmap);
            } else {

                new DownloadImageTask(gamePicture, game.getSlug())
                        .execute(game.getBackgroundImageLink().replace("https://media.rawg.io/media/games/", "https://api.rawg.io/media/resize/420/-/games/"));
//                    Log.i("INFO", game.backgroundImageLink);
            }
        }
    }
}
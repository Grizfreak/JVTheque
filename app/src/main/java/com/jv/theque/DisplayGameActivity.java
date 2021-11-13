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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class DisplayGameActivity extends AppCompatActivity {

    private ImageButton returnButton;
    private TextView title, releaseDate;
    private ImageView gameImage;
    private Game gameDisplayed;
    private Intent intent;
    private Button addButton, removeButton;
    private LinearLayout platformLayout;

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
        Bundle extras = getIntent().getExtras();
        gameDisplayed = (Game) intent.getSerializableExtra("Game");
        title.setText(gameDisplayed.getName());
        releaseDate.setText(releaseDate.getText() + " " +formatter.format(gameDisplayed.getRelease_date()));
        setPlatformButtons(gameDisplayed.getPlatforms().size());
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

    private void setPlatformButtons(int size) {
        Button[] btnWord = new Button[size];
            for (int i = 0; i < btnWord.length; i++) {
                btnWord[i] = new Button(this);
                btnWord[i].setHeight(20);
                btnWord[i].setWidth(20);
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
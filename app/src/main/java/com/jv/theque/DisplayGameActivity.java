package com.jv.theque;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.jv.theque.gameImplementation.Game;
import com.jv.theque.gameImplementation.UserGameList;
import com.jv.theque.rawgImplementation.DownloadImageTask;
import com.jv.theque.rawgImplementation.RAWGGetGameDescriptionOperation;
import com.jv.theque.tagsImplementation.Tag;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DisplayGameActivity extends AppCompatActivity {

    private ImageButton returnButton;
    private TextView title, releaseDate;
    private ImageView gameImage;
    protected Game gameDisplayed;
    private Intent intent;
    private Button removeButton;
    private FloatingActionButton addButton;
    private LinearLayout platformLayout, userTagLayout, starContainer;
    private TextView description;
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

    public synchronized void displayStars(LinearLayout starContainer) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75, 75);

        starContainer.removeAllViewsInLayout();

        for (int i = 1; i <= 5; i++) {
            boolean greyTint = i > gameDisplayed.getNote();

            ImageView tmpImgBtn = new ImageView(this);
            if (greyTint) {
                tmpImgBtn.setBackgroundResource(R.drawable.star_nope);
            } else {
                tmpImgBtn.setBackgroundResource(R.drawable.star);
            }
            tmpImgBtn.setLayoutParams(layoutParams);
            int finalI = i;
            tmpImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inList) {
                        gameDisplayed.setNote(finalI);
                        MainActivity.userData.saveToFile();
                        displayStars(starContainer);
                    }
                }
            });
            if((greyTint && inList) || !greyTint) starContainer.addView(tmpImgBtn);
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
        starContainer = findViewById(R.id.starContainer);
        Bundle extras = getIntent().getExtras();
        gameDisplayed = (Game) intent.getSerializableExtra("Game");

        userGameList = MainActivity.userData.getUserGameList();

        if (MainActivity.userData.getUserGameList().contains(gameDisplayed)) {
            inList = true;
            gameDisplayed = userGameList.find(gameDisplayed.getSlug());
        }

        title.setText(gameDisplayed.getName());
        releaseDate.setText((gameDisplayed.getRelease_date().equals(Game.DEFAULT_DATE)) ?
                (releaseDate.getText() + " inconnue") : (releaseDate.getText() + " " + formatter.format(gameDisplayed.getRelease_date())));
        setPlatformButtons(gameDisplayed.getPlatforms().size());
        displayImage(gameDisplayed, gameImage);
        updateTags();
        //TODO modifier pour gérer la suppression d'un jeu


        if (gameDisplayed.getDescription() == null || gameDisplayed.getNote() == -1) {
            try {
                JsonObject json = new RAWGGetGameDescriptionOperation(apiKey, gameDisplayed).execute("").get();
                String newGameDescription = json.get("description").getAsString();
                int newNote = Math.round(json.get("rating").getAsFloat());
                Log.i("GameDescription", newGameDescription);
                newGameDescription = newGameDescription.replaceAll("<p>", "").replaceAll("</p>", "\n").replaceAll("<br />", "\n");
                gameDisplayed.setDescription(newGameDescription);
                description.setText(description.getText() + "\n" + gameDisplayed.getDescription());

                //si l'utilisateur n'a pas déjà mis une note on met la note moyenne mis par les utilisateurs
                if (gameDisplayed.getNote() == -1) {
                    gameDisplayed.setNote(newNote);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            description.setText(description.getText() + "\n" + gameDisplayed.getDescription());
        }

        displayStars(starContainer);

        if (inList) {
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

    View.OnClickListener addANewTag = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO INTERFACE BETWEEN
            //TODO DISPLAY USERTAGS IN LAYOUT
            //Displaying all usertag available
            Toast.makeText(getApplicationContext(), "clicked add button", Toast.LENGTH_SHORT).show();
            List<Tag> result = new ArrayList<Tag>();
            CustomDialog.showAlertDialogTag(getActivity(), result);
        }
    };

    public void updateTags() {
        //TODO REFACTOR
        userTagLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if (!gameDisplayed.getUserTags().isEmpty()) {
            Log.i("MICHTOS", "SALUT MON POTE");
            AppCompatButton[] btnWord = new AppCompatButton[gameDisplayed.getUserTags().size()];
            for (int i = 0; i < btnWord.length; i++) {
                int tagColor = gameDisplayed.getUserTags().get(i).getColor();
                btnWord[i] = new AppCompatButton(this);
                btnWord[i].setBackgroundResource(R.drawable.custom_tag);
                GradientDrawable drawable = (GradientDrawable) btnWord[i].getBackground();
                drawable.setStroke(5, tagColor);
                int r=0,g=0,b=0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int color = (int)Long.parseLong(String.format("%06X", (0xFFFFFF & tagColor)), 16);
                    r = (color >> 16) & 0xFF;
                    g = (color >> 8) & 0xFF;
                    b = (color >> 0) & 0xFF;
                }
                drawable.setColor(Color.argb(25,r,g,b));
                btnWord[i].setTag(i);
                btnWord[i].setTextSize(12);
                btnWord[i].setPadding(7, 0, 7, 0);
                btnWord[i].setLayoutParams(params);
                btnWord[i].setTransformationMethod(null);
                params.setMargins(0, 0, 7, 0);
                btnWord[i].setText(gameDisplayed.getUserTags().get(i).getName());
                btnWord[i].setOnClickListener(btnClicked);
                btnWord[i].setOnLongClickListener(btnLongClicked);
                userTagLayout.addView(btnWord[i]);
            }
        }
        if (inList) {
            /*AppCompatButton btnAddTag = new AppCompatButton(this);
            btnAddTag.setText("+");
            btnAddTag.setTextSize(25);
            btnAddTag.setBackgroundResource(R.drawable.custom_button);
            btnAddTag.setOnClickListener(addANewTag);
            */
            FloatingActionButton btnAddTag = new FloatingActionButton(this);
            btnAddTag.setBackgroundTintList(ColorStateList.valueOf(Color.argb(220,0,172,193)));
            //btnAddTag.setImageResource(android.R.drawable.ic_input_add);
            btnAddTag.setForeground(getDrawable(R.drawable.plus));
            btnAddTag.setLayoutParams(params);
            params.setMargins(0, 0, 20, 0);
            btnAddTag.setOnClickListener(addANewTag);
            btnAddTag.setScaleType(ImageView.ScaleType.CENTER);

            userTagLayout.addView(btnAddTag);

        }
    }

    View.OnLongClickListener btnLongClicked = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            // Set Title and Message:
            builder.setTitle("Confirmation").setMessage("Voulez-vous retirer ce tag ?");

            //
            builder.setCancelable(true);
            builder.setIcon(R.drawable.logo);

            // Create "Yes" button with OnClickListener.
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AppCompatButton btn = (AppCompatButton) view;
                    Tag tag = MainActivity.userData.getUserTagList().find(btn.getText().toString());
                    gameDisplayed.getUserTags().remove(tag);
                    tag.removeGame(gameDisplayed);
                    updateTags();
                }
            });

            // Create "No" button with OnClickListener.
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(view.getContext(), "You choose No button",
                            Toast.LENGTH_SHORT).show();
                    //  Cancel
                    dialog.cancel();
                }
            });

            // Create AlertDialog:
            AlertDialog alert = builder.create();
            alert.show();
            return false;
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        for (int i = 0; i < btnWord.length - 1; i++) {
            int tagColor = gameDisplayed.getPlatforms().get(i).getColor();
            btnWord[i] = new AppCompatButton(this);
            btnWord[i].setBackgroundResource(R.drawable.custom_tag);
            GradientDrawable drawable = (GradientDrawable) btnWord[i].getBackground();
            drawable.setStroke(5, tagColor);
            int r=0,g=0,b=0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int color = (int)Long.parseLong(String.format("%06X", (0xFFFFFF & tagColor)), 16);
                r = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                b = (color >> 0) & 0xFF;
            }
            btnWord[i].setTextSize(12);
            btnWord[i].setPadding(20, 3, 20, 3);
            btnWord[i].setTag(i);
            btnWord[i].setTransformationMethod(null);
            drawable.setColor(Color.argb(25,r,g,b));
            btnWord[i].setLayoutParams(params);
            params.setMargins(15, 0, 0, 0);

            btnWord[i].setText(gameDisplayed.getPlatforms().get(i).toString());
            btnWord[i].setOnClickListener(btnClicked);
            platformLayout.addView(btnWord[i]);
        }
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
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
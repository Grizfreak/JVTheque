package com.jv.theque;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.jv.theque.favoritesImplementation.FavoriteSearch;
import com.jv.theque.gameImplementation.Game;
import com.jv.theque.gameImplementation.UserGameList;
import com.jv.theque.rawgImplementation.DownloadImageTask;
import com.jv.theque.rawgImplementation.RAWGGetGameDescriptionOperation;
import com.jv.theque.tagsImplementation.Tag;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DisplayGameActivity extends AppCompatActivity {

    protected Game gameDisplayed;
    private LinearLayout platformLayout;
    private LinearLayout userTagLayout;
    UserGameList userGameList;
    public final String apiKey = "6f8484cb4d6146fea90f7bd967dd96aa";
    private boolean inList = false;


    protected void checkInputUser(List<Tag> result) {
        //Vérification du choix de l'utilisateur
        if (result.size() == 0) {
            //Si celui ci est incorrect, affichage d'un message
            Toast.makeText(this, "Veuillez ajouter au minimum un tag", Toast.LENGTH_SHORT).show();
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
            tmpImgBtn.setOnClickListener(view -> {
                if (inList) {
                    gameDisplayed.setNote(finalI);
                    MainActivity.userData.saveToFile();
                    displayStars(starContainer);
                }
            });
            if(!greyTint || inList) starContainer.addView(tmpImgBtn);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_game);
        ImageButton returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(view -> onBackPressed());
        Intent intent = getIntent();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        TextView title = findViewById(R.id.GameTitle);
        ImageView gameImage = findViewById(R.id.GameImage);
        Button removeButton = findViewById(R.id.removeButton);
        TextView releaseDate = findViewById(R.id.releaseDate);
        platformLayout = findViewById(R.id.PlatformLayout);
        TextView description = findViewById(R.id.gameDescription);
        userTagLayout = findViewById(R.id.UserTagLayout);
        LinearLayout starContainer = findViewById(R.id.starContainer);
        LinearLayout linearLayout = findViewById(R.id.to_hide1);
        Space space = findViewById(R.id.to_hide2);
        getIntent().getExtras();
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
        displayImage(gameDisplayed, new WeakReference<>(gameImage));
        updateTags();

        String descriptionText = description.getText() + "\n";
        if (gameDisplayed.getDescription() == null || gameDisplayed.getNote() == -1) {
            try {
                JsonObject json = new RAWGGetGameDescriptionOperation(apiKey, gameDisplayed).execute("").get();
                String newGameDescription = json.get("description").getAsString();
                int newNote = Math.round(json.get("rating").getAsFloat());
                newGameDescription = newGameDescription.replaceAll("<p>", "").replaceAll("</p>", "\n").replaceAll("<br />", "\n");
                gameDisplayed.setDescription(newGameDescription);

                description.setText(String.format("%s%s", descriptionText, gameDisplayed.getDescription()));

                //si l'utilisateur n'a pas déjà mis une note on met la note moyenne mise par les utilisateurs
                if (gameDisplayed.getNote() == -1) {
                    gameDisplayed.setNote(newNote);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            description.setText(String.format("%s%s", descriptionText, gameDisplayed.getDescription()));
        }

        displayStars(starContainer);

        if (inList) {
            space.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);
        } else {
            removeButton.setVisibility(View.GONE);
        }
        addButton.setOnClickListener(view -> {
            //Instanciation d'une liste de résultats du dialogue
            List<Tag> result = new ArrayList<>();
            //Affichage du dialogue d'ajout des premiers tags d'un jeu
            CustomDialog.showDialogNewGameAdd(getActivity(), result, gameDisplayed);
        });
        removeButton.setOnClickListener(this::createValidationPopUp);

    }

    private void createValidationPopUp(View view1){
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(view1).getContext());

        // Titre de l'alerte et message
        builder.setTitle("Confirmation").setMessage("Voulez-vous supprimer ce jeu de votre liste ?");

        // Annulation et icône
        builder.setCancelable(true);
        builder.setIcon(R.drawable.logo);

        // Création d'un bouton "Oui" avec un OnClickListener
        builder.setPositiveButton("Oui", (dialog, id) -> {
            userGameList.removeGame(gameDisplayed);
            Toast.makeText(view1.getContext(),"Le jeu a été supprimé",Toast.LENGTH_SHORT).show();
            onBackPressed();
        });

        // Création d'un bouton "Non" avec un OnClickListener
        builder.setNegativeButton("Non", (dialog, id) -> {
            // Retour
            dialog.cancel();
        });

        // Création d'un AlertDialog
        AlertDialog alert = builder.create();
        alert.show();
    }

    final View.OnClickListener addANewTag = view -> CustomDialog.showAlertDialogTag(getActivity());

    public void updateTags() {
        userTagLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if (!gameDisplayed.getUserTags().isEmpty()) {
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
                    b = (color) & 0xFF;
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
            FloatingActionButton btnAddTag = new FloatingActionButton(this);
            btnAddTag.setBackgroundTintList(ColorStateList.valueOf(Color.argb(220,0,172,193)));
            btnAddTag.setForeground(AppCompatResources.getDrawable(this,R.drawable.plus));
            btnAddTag.setLayoutParams(params);
            params.setMargins(0, 0, 20, 0);
            btnAddTag.setOnClickListener(addANewTag);
            btnAddTag.setScaleType(ImageView.ScaleType.CENTER);

            userTagLayout.addView(btnAddTag);

        }
    }

    final View.OnLongClickListener btnLongClicked = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            // Titre de l'alerte et message
            builder.setTitle("Confirmation").setMessage("Voulez-vous retirer ce tag ?");

            // Annulation et icône
            builder.setCancelable(true);
            builder.setIcon(R.drawable.logo);

            // Création d'un bouton "Oui" avec un OnClickListener
            builder.setPositiveButton("Oui", (dialog, id) -> {
                AppCompatButton btn = (AppCompatButton) view;
                Tag tag = MainActivity.userData.getUserTagList().find(btn.getText().toString());
                // Suppression du tag
                gameDisplayed.getUserTags().remove(tag);
                tag.removeGame(gameDisplayed);
                updateTags();
            });

            // Création d'un bouton "Non" avec un OnClickListener
            builder.setNegativeButton("Non", (dialog, id) -> {
                // Retour
                dialog.cancel();
            });

            // Création d'un AlertDialog
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    };
    final View.OnClickListener btnClicked = v -> {
        Object tag = v.getTag();
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
                b = (color) & 0xFF;
            }
            btnWord[i].setTextSize(15);
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

    void displayImage(Game game, WeakReference<ImageView> gamePicture) {
        if (game.getBackgroundImageLink() != null) {

            if (new File(App.getAppContext().getCacheDir(), game.getSlug() + ".png").exists()) {
                File gamePicFile = new File(App.getAppContext().getApplicationContext().getCacheDir(), game.getSlug() + ".png");
                Bitmap bitmap = BitmapFactory.decodeFile(gamePicFile.getAbsolutePath());
                gamePicture.get().setImageBitmap(bitmap);
            } else {

                new DownloadImageTask(gamePicture, game.getSlug())
                        .execute(game.getBackgroundImageLink().replace("https://media.rawg.io/media/games/", "https://api.rawg.io/media/resize/420/-/games/"));
            }
        }
    }

    private DisplayGameActivity getActivity() {
        return this;
    }
}
package com.jv.theque;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder>  {
    List<Game> gameList;
    GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.display(gameList.get(position));
    }

    @Override
    public int getItemCount() {
        if(gameList != null){
            return gameList.size();
        }
        else return 0;
    }


    class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView gameName;
        private TextView gamePlat;
        private ImageView gamePicture;

        GameViewHolder(View itemView) {
            super(itemView);

            gamePicture = (ImageView) itemView.findViewById(R.id.image2);
            gameName = (TextView) itemView.findViewById(R.id.name2);
            gamePlat = (TextView) itemView.findViewById(R.id.platform2);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.getContext(),String.valueOf(itemView.getId()), Toast.LENGTH_SHORT);
        }


        void display(Game game) {
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

            gameName.setText(game.getName());
            if (game.getTags().get("platform") != null) {
                //TODO with map
                List<Tag> tempo = game.getTags().get("platform");
                Log.e("liste",String.valueOf(tempo.isEmpty()));
                if(!tempo.isEmpty()){
                    gamePlat.setText(tempo.get(0).getName());
                }
            }
        }



    }
}


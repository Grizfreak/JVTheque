package com.jv.theque.RecyclerViewUsages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.theque.TagsImplementation.RAWGTag;
import com.jv.theque.TagsImplementation.Tag;
import androidx.recyclerview.widget.RecyclerView;

import com.jv.theque.GameImplementation.Game;
import com.jv.theque.MainActivity;
import com.jv.theque.R;
import com.jv.theque.RAWGImplementation.DownloadImageTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder>  {
    List<Game> gameList;
    public GameAdapter(List<Game> gameList) {
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
                if (new File(MainActivity.getContext().getApplicationContext().getCacheDir(), game.getSlug() + ".png").exists()) {
                    File gamePicFile = new File(MainActivity.getContext().getApplicationContext().getCacheDir(), game.getSlug() + ".png");
                    Bitmap bitmap = BitmapFactory.decodeFile(gamePicFile.getAbsolutePath());
                    gamePicture.setImageBitmap(bitmap);
                } else {
                    new DownloadImageTask(gamePicture, game.getSlug())
                            .execute(game.getBackgroundImageLink().replace("https://media.rawg.io/media/games/", "https://api.rawg.io/media/resize/420/-/games/"));
                }
            }

            gameName.setText(game.getName());

            //Methode pour afficher la première plateforme du jeu
            List<Tag> tmpList = game.getPlatforms();
            if (tmpList != null){
                if(!tmpList.isEmpty()){
                    for (Tag t : tmpList){
                        Log.e("inadapter",t.getName());
                    }
                    gamePlat.setText(tmpList.get(0).getName());
                }
            }
        }



    }
}


package com.jv.theque;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
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
        return gameList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        private TextView gameName;
        private TextView gamePlat;
        private ImageView gamePicture;

        GameViewHolder(View itemView) {
            super(itemView);

            gamePicture = (ImageView) itemView.findViewById(R.id.image2);
            gameName = (TextView) itemView.findViewById(R.id.name2);
            gamePlat = (TextView) itemView.findViewById(R.id.platform2);

        }

        void display(Game game) {
            new DownloadImageTask((ImageView) gamePicture)
                    .execute(game.backgroundImageLink);

            gameName.setText(game.name);
            if (game.platforms != null) {
                gamePlat.setText(game.platforms[0].platform.name);
            }
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                if(result == null){
                    return;
                }
                bmImage.setImageBitmap(result);
            }
        }

    }
}


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
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    List<RAWG_Game> RAWGGameList;

    GameAdapter(List<RAWG_Game> RAWGGameList) {
        this.RAWGGameList = RAWGGameList;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.game_layout, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.display(RAWGGameList.get(position));
    }

    @Override
    public int getItemCount() {
        return RAWGGameList.size();
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

        void display(RAWG_Game RAWGGame) {
            if(RAWGGame.backgroundImageLink != null){

                if(new File(App.getAppContext().getCacheDir(), RAWGGame.slug+".png").exists()){
                    File gamePicFile = new File(App.getAppContext().getCacheDir(), RAWGGame.slug+".png");
                    Bitmap bitmap = BitmapFactory.decodeFile(gamePicFile.getAbsolutePath());
                    gamePicture.setImageBitmap(bitmap);
                }else{

                    new DownloadImageTask(gamePicture, RAWGGame.slug)
                            .execute(RAWGGame.backgroundImageLink);
                }
                }

            gameName.setText(RAWGGame.name);
            if (RAWGGame.platforms != null) {
                gamePlat.setText(RAWGGame.platforms[0].platform.name);
            }
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            String gameSlug;

            public DownloadImageTask(ImageView bmImage, String gameSlug){
                this.bmImage = bmImage; this.gameSlug = gameSlug;
            }

         public  Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    File file = new File(App.getAppContext().getCacheDir(), gameSlug+".png");
                    file.createNewFile();
                    FileOutputStream filO = new FileOutputStream(file);
                    OutputStream os = new BufferedOutputStream(filO);
                    mIcon11.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.close();
                    filO.close();

                    Log.println(Log.DEBUG, "ChargementImage", "Image pour le slug "+gameSlug+" chargée dans le fichier "+file.getPath());

                    bmImage.setImageBitmap(mIcon11);

                } catch (Exception e) {
                    Log.e("Error", " " +e);
                    //e.printStackTrace();
                }

             return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
                Log.println(Log.INFO,"Image", "one image posted");
            }

        }

    }
}


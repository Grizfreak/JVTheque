package com.jv.theque;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String gameSlug;

    public DownloadImageTask(ImageView bmImage, String gameSlug) {
        this.bmImage = bmImage;
        this.gameSlug = gameSlug;
    }

    public Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            File file = new File(App.getAppContext().getCacheDir(), gameSlug + ".png");
            file.createNewFile();
            FileOutputStream filO = new FileOutputStream(file);
            OutputStream os = new BufferedOutputStream(filO);
            mIcon11.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            filO.close();

            Log.println(Log.DEBUG, "ChargementImage", "Image pour le slug " + gameSlug + " chargée dans le fichier " + file.getPath());

            // TODO : Je sais pas si c'est important mais la ligne juste en dessous provoque une erreur ("Only the original thread that created a view hierarchy can touch its views")
            //  parce l'instruction s'exécute dans un thread différent du thread UI, donc ca pose problème lors du changement de page avec les fragments
            // TODO : Spoiler Simon, Maxime il l'a mis mais je crois que son truc marche même pas donc au pire
            //bmImage.setImageBitmap(mIcon11);

        } catch (Exception e) {
            Log.e("Error", " " + e);
            //e.printStackTrace();
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        Log.println(Log.INFO, "Image", "one image posted");
    }


}

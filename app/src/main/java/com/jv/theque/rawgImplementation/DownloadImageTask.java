package com.jv.theque.rawgImplementation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.jv.theque.App;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    final WeakReference<ImageView> bmImage;
    final String gameSlug;

    public DownloadImageTask(WeakReference<ImageView> bmImage, String gameSlug) {
        this.bmImage = bmImage;
        this.gameSlug = gameSlug;
    }

    public Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            File file = new File(App.getAppContext().getApplicationContext().getCacheDir(), gameSlug + ".png");
            if (file.createNewFile()){
                FileOutputStream filO = new FileOutputStream(file);
                OutputStream os = new BufferedOutputStream(filO);
                mIcon11.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
                filO.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.get().setImageBitmap(result);
    }


}

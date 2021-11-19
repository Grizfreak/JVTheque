package com.jv.theque;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.background_splash);

        new Handler().postDelayed(() -> {
            SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
            SplashScreen.this.finish();
        }, 1500);
    }
}

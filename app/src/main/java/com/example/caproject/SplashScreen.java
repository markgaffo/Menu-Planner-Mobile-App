package com.example.caproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {
    ImageView logo,splashBackground;
    TextView splashName;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //getting logo and backgrounds
        logo = findViewById(R.id.splashlogo);
        splashBackground = findViewById(R.id.splashbackground);
        splashName = findViewById(R.id.splashName);
        lottieAnimationView = findViewById(R.id.splashLot);

        //moving the logo background and animation with timers.
        splashBackground.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        splashName.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent loginIntent = new Intent(SplashScreen.this,LoginActivity.class);
                SplashScreen.this.startActivity(loginIntent);
                SplashScreen.this.finish();
            }
            //time to swap page
        }, 4500);
    }
}
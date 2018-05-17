package com.example.felix.lab_googlefirebaseactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    private static int ROTATE_TIME_OUT = 3000;
    private static int SPLASH_TIME_OUT = 5000;

    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        image_view = findViewById(R.id.splash_image);

        scrollupAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateAnimation();
            }
        }, ROTATE_TIME_OUT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void scrollupAnimation(){

        Animation scrollup_animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        scrollup_animation.reset();

        RelativeLayout relative_layout1 = findViewById(R.id.relative_layout);
        relative_layout1.clearAnimation();
        relative_layout1.startAnimation(scrollup_animation);

        scrollup_animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        scrollup_animation.reset();

        image_view.clearAnimation();
        image_view.startAnimation(scrollup_animation);
    }

    private void rotateAnimation(){
        Animation rotate_animation = AnimationUtils.loadAnimation(this, R.anim.rotation);

        rotate_animation.reset();

        image_view.clearAnimation();
        image_view.startAnimation(rotate_animation);
    }
}

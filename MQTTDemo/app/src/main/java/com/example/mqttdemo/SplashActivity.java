package com.example.mqttdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView loading;
   ProgressBar power;

    ImageView imageUtman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       Thread logoAnimation=new Thread(){
           @Override
           public void run(){
               loading=findViewById(R.id.TextLoading);
               power=findViewById(R.id.power);
               imageUtman=findViewById(R.id.utman);
               ImageView logo =findViewById(R.id.splash_logo);
               Animation  animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_intro_author );
               logo.startAnimation(animation);
           }
       };
            logoAnimation.start();



        Thread redirect=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3500);

                    Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        redirect.start();







    }
}
package com.softhub.shivmudra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;

    private Animation left;
    private Animation right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        logo = (ImageView) findViewById(R.id.logo);

        left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromleft);
        right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fromright);


        //and.animate().alpha(1F).setDuration(2500);
        logo.setAnimation(right);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                }
            }
        };
        t.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
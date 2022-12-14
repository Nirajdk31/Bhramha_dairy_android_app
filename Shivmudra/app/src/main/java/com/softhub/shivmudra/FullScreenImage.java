package com.softhub.shivmudra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

public class FullScreenImage extends AppCompatActivity {


    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getSupportActionBar().hide();


        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle b = getIntent().getExtras();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView name = (TextView) findViewById(R.id.name);

        Picasso.with(getApplicationContext()).load(b.getString("image")).into(imageView);
        name.setText(b.getString("name"));


        imageView.setOnTouchListener(new ImageMatrixTouchHandler(imageView.getContext()));

    }
}
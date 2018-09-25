package com.androidtest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RatingBar;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class RatingBarActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingbar);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(RatingBarActivity.this,"评分:" + rating,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

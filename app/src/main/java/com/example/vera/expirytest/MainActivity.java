package com.example.vera.expirytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean IS_DEBUG_BUILD = BuildConfig.DEBUG;

        //Debug and beta builds expire after 90 days. Final release builds expire after 292 million years.

        long EXPIRY_DATE = IS_DEBUG_BUILD ? BuildConfig.BuildTimestamp + 90 * 24 * 60 * 60 * 1000L : Long.MAX_VALUE;
        //if is debug build, start ExpiryActivity
        if(IS_DEBUG_BUILD) {
            startActivity(new Intent(MainActivity.this, ExpiryActivity.class));
        }
    }
}

package com.example.vera.expirytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean IS_DEBUG_BUILD = BuildConfig.DEBUG;
        //Debug expire after 90 days. Final release builds expire after 292 million years.
        long EXPIRY_DATE = IS_DEBUG_BUILD ? BuildConfig.BuildTimestamp + 90 * 24 * 60 * 60 * 1000L : Long.MAX_VALUE;
        //打印过期时间还有多少
        long now = System.currentTimeMillis();
        long daysBeforeExpiry =
                (EXPIRY_DATE - now) / 1000 / 60 / 60 / 24;
        Log.i("EXPIRY", "days before expiry:" + daysBeforeExpiry);

        //如果过期，就跳转到ExpiryActivity
        if(System.currentTimeMillis() >= EXPIRY_DATE) {
            startActivity(new Intent(MainActivity.this, ExpiryActivity.class));
        }
    }
}

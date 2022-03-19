package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences share = getSharedPreferences("Authorization", Context.MODE_PRIVATE);
                String token = share.getString("token", null);

                if (token == null){
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(SplashActivity.this,HomeScreen.class);
                    startActivity(intent);
                }

                finish();
            }
        },1000
        );

    }
}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences share = getSharedPreferences("Auth", Context.MODE_PRIVATE);
        String token = share.getString("token", null);

        if (token == null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this,HomeScreen.class);
            startActivity(intent);
        }

        finish();
    }
}
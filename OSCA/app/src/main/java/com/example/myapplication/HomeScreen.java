package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeScreen extends AppCompatActivity {
    LinearLayout layout1, layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        TextView logoutBtn = (TextView) findViewById(R.id.logoutBtn);
        layout1 = findViewById(R.id.UpcoomingIncomeCard);
        layout2 = findViewById(R.id.PastIncomeCard);

        SharedPreferences share = getSharedPreferences("Authorization", Context.MODE_PRIVATE);
        String token = share.getString("token", null);

        Executor e = Executors.newSingleThreadExecutor();
        e.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("Pass", "2");

                URL url = null;

                try {
                    url = new URL("http://10.0.2.2:8080/OSCA_war_exploded/AndroidLoginServlet");
                    HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty ("Authorization", "Bearer " + token);
                    JSONObject obj = new JSONObject();

//                    DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());
//                    out.writeBytes(obj.toString());
//                    out.close();
                    Log.d("Pass", String.valueOf(httpConn.getResponseCode()));

                    StringBuilder sb = new StringBuilder();
                    if (httpConn.getResponseCode() == 200){
                        InputStream is = httpConn.getInputStream();
                        BufferedReader read = new BufferedReader(new InputStreamReader(is));
                        String line = null;

                        while((line = read.readLine()) != null){
                            sb.append(line);
                            sb.append('\r');
                        }

                        read.close();
                        Log.d("login", sb.toString());
                    }

                    httpConn.disconnect();

                    //if else
                    convertToJSON(sb.toString());


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Authorization", MODE_PRIVATE).edit();
                editor.putString("token", null);
                editor.apply();
                Intent intent = new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, PaymentDetails.class);
                startActivity(intent);
                finish();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, PastPaymentDetails.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public void convertToJSON(String json) throws JSONException {
        TextView name = (TextView) findViewById(R.id.welcome);
        TextView pastIncome = (TextView) findViewById(R.id.pastIncome);
        TextView futureIncome = (TextView) findViewById(R.id.upcomingIncome);


        JSONObject obj = new JSONObject(json);

//        Log.d("k1", obj.getString("fullname"));
//        Log.d("k2", String.valueOf(obj.getDouble("fIncome")));
//        Log.d("k3", String.valueOf(obj.getDouble("pIncome")));

        name.append(obj.getString("fullname"));
        futureIncome.setText(obj.getString("fIncome"));
        pastIncome.setText(obj.getString("pIncome"));


    }

}
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private boolean doubleTapToExit = false;
    private EditText email;
    private EditText password;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.emailVal);
        password = (EditText) findViewById(R.id.passVal);
        Button login = (Button) findViewById(R.id.btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Executor e = Executors.newSingleThreadExecutor();
                e.execute(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;

                        try {
                            url = new URL("http://10.0.2.2:8080/OSCA_war_exploded/AndroidLoginServlet");
                            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
                            httpConn.setRequestMethod("POST");

                            JSONObject obj = new JSONObject();
                            obj.put("email", email.getText());
                            obj.put("password", password.getText());

                            DataOutputStream out = new DataOutputStream(httpConn.getOutputStream());

                            out.writeBytes(obj.toString());
                            out.close();
                            Log.d("Pass", String.valueOf(httpConn.getResponseCode()));


                            StringBuilder sb = new StringBuilder();

                            if (httpConn.getResponseCode() == 200){
                                InputStream is = httpConn.getInputStream();
                                BufferedReader read = new BufferedReader(new InputStreamReader(is));
//                                 StringBuilder sb = new StringBuilder();
                                String line = null;

                                while((line = read.readLine()) != null){
                                    sb.append(line);
                                    sb.append('\r');
                                }

                                read.close();
                                Log.d("login", sb.toString());
                            }

                            httpConn.disconnect();
                            convertToJSON(sb.toString());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (doubleTapToExit) {
            super.onBackPressed();
            return;
        }

        this.doubleTapToExit = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleTapToExit=false;
            }
        }, 500);
    }



    public void convertToJSON(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);

        int ut = obj.getInt("userType");


        if (ut == 4){
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Authorization", MODE_PRIVATE).edit();
            editor.putString("token", obj.getString("token"));
            editor.apply();

            Intent intent = new Intent(this,HomeScreen.class);
            startActivity(intent);

            finish();
        }

        else{
            Log.d("message =>", "Error!!!! Try again");

            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Email or password is incorrect", Toast.LENGTH_LONG).show();
                    }
                });
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
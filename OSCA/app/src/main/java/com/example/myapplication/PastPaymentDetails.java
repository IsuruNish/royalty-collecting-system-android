package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.payment.PaymentAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PastPaymentDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        TextView backBtn = (TextView) findViewById(R.id.backBtn);

        SharedPreferences share = getSharedPreferences("Authorization", Context.MODE_PRIVATE);
        String token = share.getString("token", null);

        Executor e = Executors.newSingleThreadExecutor();
        e.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;

                try {
                    url = new URL("http://10.0.2.2:8080/OSCA_war_exploded/AndroidPastPaymentsServlet");
                    HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
                    httpConn.setRequestMethod("GET");
                    httpConn.setRequestProperty ("Authorization", "Bearer " + token);
//                    JSONObject obj = new JSONObject();

                    StringBuilder sb = new StringBuilder();
                    ArrayList<ArrayList<String>> newList = new ArrayList<>();

                    if (httpConn.getResponseCode() == 200){

                        InputStream is = httpConn.getInputStream();
                        BufferedReader read = new BufferedReader(new InputStreamReader(is));
                        String line = null;

                        while((line = read.readLine()) != null){
                            sb.append(line);
                            Log.e("the mag => ",line);
                            sb.append('\r');
                        }

                        read.close();
                        Log.e("login", sb.toString());


                        JSONArray arrObj = new JSONArray(String.valueOf(sb));
                        ArrayList<String> listdata = new ArrayList<String>();

                        for(int i=0; i<arrObj.length(); i++){
                            listdata.add(arrObj.getString(i));
                        }

                        System.out.println(listdata.size());

//                        ArrayList<ArrayList<String>> newList = new ArrayList<>();

                        for(int i=0; i<listdata.size(); i++){
                            ArrayList<String> temp = new ArrayList<>();
                            JSONArray objectArr = new JSONArray(listdata.get(i));
                            temp.add(objectArr.getString(0));
                            temp.add(objectArr.getString(1));
                            newList.add(temp);
                        }

                    }

                    httpConn.disconnect();



                    if (newList.size() != 0){
                        recyclerView = findViewById(R.id.Rview1);
                        paymentAdapter = new PaymentAdapter(newList);
                        recyclerView.setAdapter(paymentAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PastPaymentDetails.this));
                    }
                    else{
                        Intent intent = new Intent(PastPaymentDetails.this, PastBlank.class);
                        startActivity(intent);
                        finish();
                    }



                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastPaymentDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
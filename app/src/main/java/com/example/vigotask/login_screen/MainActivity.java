package com.example.vigotask.login_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.vigotask.R;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    MaterialTextView resTV, accessTokenTV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingUpIds();

//        Fetching data from Intent
        Intent intent = getIntent();
        String res = intent.getStringExtra("response_intent");
        String token = intent.getStringExtra("access_token_intent");

//        Fetching data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginResponse", MODE_PRIVATE);
        String savedRes = sharedPreferences.getString("success_response", "");

        resTV.setText("Response Received: \n" +res);
        accessTokenTV.setText("Access Token: \n" +token);

    }

    private void settingUpIds() {
        resTV = findViewById(R.id.response_received);
        accessTokenTV = findViewById(R.id.access_token);
    }
}
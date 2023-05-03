package com.example.vigotask.login_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vigotask.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText partnerIdEditText;
    TextInputEditText userNameEditText;
    TextInputEditText passwordEditText;
    MaterialButton loginButton;
    String publicUrl = "http://tusker.smarterp.live/token";
    String ipAddress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settingUpIds();

        partnerIdEditText.setText("logic");
        userNameEditText.setText("vigo60");
        passwordEditText.setText("Test@1234");
        loginButton.setOnClickListener(view -> {
            try {
                login();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void settingUpIds() {
        partnerIdEditText = findViewById(R.id.partner_id);
        userNameEditText = findViewById(R.id.user_name);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
    }


    private void login() throws UnsupportedEncodingException {
//        String un = "logic%2Fvigo60%23SM-M317F%2Fsamsung%2Fsamsung%2F31%2F12%2FBTgzdOW0bj0qlPP2DnhiYM0ysYsFzrWGe2CFmba4M%2F192.168.1.52";
        String partnerId = Objects.requireNonNull(partnerIdEditText.getText()).toString();
        String userName = Objects.requireNonNull(userNameEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        String str_user = partnerId + "/" + userName;
        String deviceDetails = Build.MODEL
                + "/" + Build.MANUFACTURER
                + "/" + Build.BRAND
                + "/" + Build.VERSION.SDK_INT
                + "/" + Build.VERSION.RELEASE;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        }

        String detailsWithIP = deviceDetails + "/" + "BTgzdOW0bj0qlPP2DnhiYM0ysYsFzrWGe2CFmba4M" + "/" + ipAddress;

        String user = str_user + "#" + detailsWithIP;
        String url = "http://www.lncerp.com/token";
        String params = "grant_type=" + URLEncoder.encode("password", "UTF-8") +
                "&username=" + URLEncoder.encode(user, "UTF-8") +
                "&password=" + URLEncoder.encode(password, "UTF-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), params);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();
                    Log.d("OK_RESPONSE", "onResponse: " + responseBody);
                    // Save success response in Shared Preferences
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginResponse", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("success_response", responseBody);
                    editor.apply();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String accessToken = jsonObject.getString("access_token");
                        Log.d("OK_RESPONSE", "accessToken: " + accessToken);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .putExtra("response_intent", responseBody)
                                .putExtra("access_token_intent", accessToken));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

}
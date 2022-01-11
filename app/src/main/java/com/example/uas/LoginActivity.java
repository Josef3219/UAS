package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.uas.Config.config;
import com.example.uas.Controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText logusername, logpassword;
    Button login;
    String action ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        login.setOnClickListener(v -> {
            action = "login";
            loginvalidate();
        });
    }

    private void initViews() {
        logusername = findViewById(R.id.LogETUsername);
        logpassword = findViewById(R.id.LogETPassword);
        login = findViewById(R.id.btn_login);
    }

    private void loginvalidate() {
        StringRequest strReqLogin = new StringRequest(Request.Method.POST, config.senddata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                //berdasarkan admin atau user
                if (!jsonObject.getBoolean("error") && jsonObject.getString("peran").equals("admin")) {
                    Toast.makeText(LoginActivity.this, "Login sebagai admin", Toast.LENGTH_LONG).show();
                    Intent keHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                    keHomeActivity.putExtra("username", String.valueOf(logusername.getText()));
                    keHomeActivity.putExtra("peran", jsonObject.getString("peran"));
                    keHomeActivity.putExtra("fullname", "");
                    startActivity(keHomeActivity);
                    finish();
                } else if (!jsonObject.getBoolean("error") && jsonObject.getString("peran").equals("user")) {
                    Toast.makeText(LoginActivity.this, "Login sebagai user", Toast.LENGTH_LONG).show();
                    Intent keHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                    keHomeActivity.putExtra("username", String.valueOf(logusername.getText()));
                    keHomeActivity.putExtra("peran", jsonObject.getString("peran"));
                    keHomeActivity.putExtra("fullname", jsonObject.getString("fullname"));
                    startActivity(keHomeActivity);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    logusername.setText(null);
                    logpassword.setText(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", String.valueOf(logusername.getText()));
                params.put("password", String.valueOf(logpassword.getText()));
                params.put("action", action);

                return params;
            }
        };

        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReqLogin);
    }


    public void register(View view) {
        Intent keRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(keRegisterActivity);
        finish();
    }
}
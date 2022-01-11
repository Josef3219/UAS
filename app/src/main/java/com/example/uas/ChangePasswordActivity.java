package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentpassword, newpassword, confirmnewpassword ;
    Button changepassword;
    String action ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // inisialisasi id di xml
        initViews();

        Bundle bundle = getIntent().getExtras();

        changepassword.setOnClickListener(v -> {
            action = "change";
            change(bundle.getString("username"));
        });

    }

    private void initViews() {
        currentpassword = findViewById(R.id.CPCurrentPassword);
        newpassword = findViewById(R.id.CPNewPassword);
        confirmnewpassword = findViewById(R.id.CPConfirmNewPassword);
        changepassword = findViewById(R.id.btnChangePassword);
    }

    private void change(String username) {
        action = "change";
        StringRequest strReq = new StringRequest(Request.Method.POST, config.senddata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keLoginActivity = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(keLoginActivity);
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_LONG).show();
                    currentpassword.setText(null);
                    newpassword.setText(null);
                    confirmnewpassword.setText(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", String.valueOf(currentpassword.getText()));
                params.put("newpassword", String.valueOf(newpassword.getText()));
                params.put("confirmnewpassword", String.valueOf(confirmnewpassword.getText()));
                params.put("action", action);

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }
}
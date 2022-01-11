package com.example.uas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.uas.Config.config;
import com.example.uas.Controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText regusername, regemail, regpassword;
    Button regis;
    String action = "";
    RadioButton radioBtnAdmin;
    String peran = "";
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // inisialisasi id di xml
        initViews();

        regis.setOnClickListener(v -> {
            action = "register";
            //mengecek gender yang dipilih berdasarkan radio button
            checkradiobutton();
            senddata();
        });
    }

    private void initViews() {
        regusername = findViewById(R.id.RegETUsername);
        regemail = findViewById(R.id.RegETEmail);
        regpassword = findViewById(R.id.RegETPassword);
        regis = findViewById(R.id.btn_regis);
        radioBtnAdmin = findViewById(R.id.RadBtnAdmin);
        radioGroup = findViewById(R.id.radiogroup);
    }

    private void senddata() {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.senddata, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                if (!jObj.getBoolean("error")) {
                    Toast.makeText(RegisterActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                    regusername.setText(null);
                    regemail.setText(null);
                    regpassword.setText(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("username", String.valueOf(regusername.getText()));
                params.put("email", String.valueOf(regemail.getText()));
                params.put("peran", peran);
                params.put("password", String.valueOf(regpassword.getText()));
                params.put("action", action);

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    public void login(View view) {
        Intent keLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(keLoginActivity);
        finish();
    }

    public void checkradiobutton() {
        if (radioBtnAdmin.isChecked()){
            peran = "admin";
        }
        else {
            peran = "user";
        }
    }
}
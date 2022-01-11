package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class DetailProjectActivity extends AppCompatActivity {
    TextView detailnamaproject, detailpicproject, detailstartdateproject, detailenddateproject;
    Bundle bundle;
    Button btneditproject, btndeleteproject;
    String action = "";
    String usernameFromHome, peran, fullname, namaproject;
    LinearLayout llbtneditproject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_project);

        initViews();
        bundle = getIntent().getExtras();

        if (bundle != null) {
            namaproject = bundle.getString("namaproject");

            detailnamaproject.setText(namaproject);
            detailpicproject.setText(bundle.getString("pic"));
            detailstartdateproject.setText(bundle.getString("startdate"));
            detailenddateproject.setText(bundle.getString("enddate"));
            usernameFromHome = bundle.getString("username");
            peran = bundle.getString("peran");
            fullname = bundle.getString("fullname");
        }

        // atur visibility button untuk user
        if (peran.equals("admin")) {
            btneditproject.setVisibility(View.VISIBLE);
            btndeleteproject.setVisibility(View.VISIBLE);
        } else {
            btneditproject.setVisibility(View.INVISIBLE);
            btndeleteproject.setVisibility(View.INVISIBLE);
            llbtneditproject.setVisibility(View.INVISIBLE);
        }

        btneditproject.setOnClickListener(v -> {
            Intent keEditActivity = new Intent(
                    DetailProjectActivity.this, EditProjectActivity.class);
            keEditActivity.putExtra("id", bundle.getString("id"));
            keEditActivity.putExtra("namaproject", bundle.getString("namaproject"));
            keEditActivity.putExtra("pic", bundle.getString("pic"));
            keEditActivity.putExtra("startdate", bundle.getString("startdate"));
            keEditActivity.putExtra("enddate", bundle.getString("enddate"));
            keEditActivity.putExtra("username", bundle.getString("username"));
            keEditActivity.putExtra("peran", bundle.getString("peran"));
            keEditActivity.putExtra("fullname", bundle.getString("fullname"));
            startActivity(keEditActivity);
        });

        btndeleteproject.setOnClickListener(v -> {
            action = "delete";
            deletedata(bundle.getString("id"), usernameFromHome);
        });
    }

    private void deletedata(String id, String username) {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.projectdata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(DetailProjectActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keHomeActivity = new Intent(DetailProjectActivity.this, HomeActivity.class);
                    keHomeActivity.putExtra("username", username);
                    startActivity(keHomeActivity);
                    finish();
                } else {
                    Toast.makeText(DetailProjectActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("action", action);
                params.put("pic", bundle.getString("pic"));

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    private void initViews() {
        btneditproject = findViewById(R.id.btnEditProject);
        btndeleteproject = findViewById(R.id.btnDeleteProject);
        detailnamaproject = findViewById(R.id.TVNamaProject);
        detailpicproject = findViewById(R.id.TVPICProject);
        detailstartdateproject = findViewById(R.id.TVStartDate);
        detailenddateproject = findViewById(R.id.TVEndDate);
        llbtneditproject = findViewById(R.id.LLBtnDetailProject);
    }
}
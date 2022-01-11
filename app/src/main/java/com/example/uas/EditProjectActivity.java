package com.example.uas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProjectActivity extends AppCompatActivity {
    EditText namaproject, enddateproject;
    TextView picproject, startdateproject;
    Button btnUpdate;
    String action = "edit", id, username,  peran, fullname;
    Bundle bundle;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        // inisialisasi id di xml
        initViews();
        // mengatur end date dan nilainya
        toggleEndDate();

        bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getString("id");
            username = bundle.getString("username");
            peran = bundle.getString("peran");
            fullname = bundle.getString("fullname");
            namaproject.setText(bundle.getString("namaproject"));
            picproject.setText(bundle.getString("pic"));
            startdateproject.setText(bundle.getString("startdate"));
            enddateproject.setText(bundle.getString("enddate"));
        }

        btnUpdate.setOnClickListener(v -> editdata(id, username, peran, fullname));
    }

    private void editdata(String id, String username, String peran, String fullname) {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.projectdata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(EditProjectActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keProjectListActivity = new Intent(EditProjectActivity.this, ProjectListActivity.class);
                    keProjectListActivity.putExtra("username", username);
                    keProjectListActivity.putExtra("peran", peran);
                    keProjectListActivity.putExtra("fullname", fullname);
                    startActivity(keProjectListActivity);
                    finish();
                } else {
                    Toast.makeText(EditProjectActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("namaproject", String.valueOf(namaproject.getText()));
                params.put("pic", String.valueOf(picproject.getText()));
                params.put("startdate", String.valueOf(startdateproject.getText()));
                params.put("enddate", String.valueOf(enddateproject.getText()));
                params.put("action", action);

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    private void toggleEndDate() {
        //mendapatkan waktu hari ini berdasarkan tanggal, bulan, dan tahun
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        enddateproject.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditProjectActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show(); // memunculkan dialog kalender (date picker)
            setListener = (datePicker, year1, month1, day1) -> {
                month1 = month1 +1;
                String date = day1 +"/"+ month1 +"/"+ year1;
                enddateproject.setText(date);
            };
        });
    }

    private void initViews() {
        namaproject = findViewById(R.id.ETDataNamaProject);
        picproject = findViewById(R.id.TVDataPICProject);
        startdateproject = findViewById(R.id.TVDataStartDateProject);
        enddateproject = findViewById(R.id.ETDataEndDateProject);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
}
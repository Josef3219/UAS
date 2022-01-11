package com.example.uas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddProjectActivity extends AppCompatActivity {
    TextView startdate, enddate, namaproject;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner PIC;
    ArrayList<String> namaIntern = new ArrayList<>();
    Button btnaddproject;
    String action = "add";
    Bundle bundle;
    String username, peran, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        // inisialisasi id di xml
        initViews();
        // menentukan start date
        toggleStartDate();
        // menentukan isi dropdown PIC berupa nama intern
        toggleSpinnerPIC();

        bundle = getIntent().getExtras();

        if (bundle != null){
            username = bundle.getString("username");
            peran = bundle.getString("peran");
            fullname = bundle.getString("fullname");
        }

        btnaddproject.setOnClickListener(v -> {
            StringRequest strReq = new StringRequest(Request.Method.POST, config.projectdata, response -> {
                try {
                    JSONObject jObj = new JSONObject(response);
                    if (!jObj.getBoolean("error")) {
                        Toast.makeText(AddProjectActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                        Intent keProjectListActivity = new Intent(AddProjectActivity.this, ProjectListActivity.class);
                        keProjectListActivity.putExtra("username", username);
                        keProjectListActivity.putExtra("peran", peran);
                        keProjectListActivity.putExtra("fullname", fullname);
                        startActivity(keProjectListActivity);
                        finish();
                    } else {
                        Toast.makeText(AddProjectActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                        namaproject.setText(null);
                        startdate.setText(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> VolleyLog.d("Error: " + error.getMessage())){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("namaproject", String.valueOf(namaproject.getText()));
                    params.put("pic", PIC.getSelectedItem().toString());
                    params.put("startdate", String.valueOf(startdate.getText()));
                    params.put("enddate", String.valueOf(enddate.getText()));
                    params.put("action", action);

                    return params;
                }
            };
            AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
        });

    }

    private void toggleStartDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        startdate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddProjectActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
            setListener = (datePicker, year1, month1, day1) -> {
                month1 = month1 +1;
                String date = day1 +"/"+ month1 +"/"+ year1;
                startdate.setText(date);
            };
        });
    }

    private void initViews() {
        startdate = findViewById(R.id.TVStartDate);
        enddate = findViewById(R.id.TVEndDate);
        PIC = findViewById(R.id.ETPICProject);
        btnaddproject = findViewById(R.id.btnAddNewProject);
        namaproject = findViewById(R.id.ETNamaProject);
    }

    private void toggleSpinnerPIC() {
        String action = "fetchName";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.projectdata, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                JSONObject header = response.getJSONObject("datanama");
                Log.e("isi header", header.toString());

                Iterator<String> iterator = header.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject nama = (JSONObject) header.get(key);

                    String temp = nama.getString("nama");
                    Log.e("fnefne", temp);

                    namaIntern.add(temp);
                }
                ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, namaIntern);
                PIC.setAdapter(spinnerAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error:" + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", action);
                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
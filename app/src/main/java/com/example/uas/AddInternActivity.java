package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.uas.Config.config;
import com.example.uas.Controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddInternActivity extends AppCompatActivity {
    EditText namanewintern, umurnewintern, emailnewintern, alamatnewintern, telponnewintern, projectnewintern;
    Button clearbutton, addbutton;
    RadioButton jenkelnewintern;
    String gender = "";
    String action = "";
    Spinner performanewintern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_intern);
        // inisialisasi id di xml
        initViews();
        // mengatur isi dropdown performance
        toggleSpinnerPerformance();

        clearbutton.setOnClickListener(v -> {
            namanewintern.setText(null);
            umurnewintern.setText(null);
            emailnewintern.setText(null);
            alamatnewintern.setText(null);
            telponnewintern.setText(null);
        });

        addbutton.setOnClickListener(v -> {
            action = "add";
            checkradiobutton();
            addnewdata();
        });
    }

    private void addnewdata() {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.interndata, response -> {
            try {
                JSONObject jObj = new JSONObject(response);
                if (!jObj.getBoolean("error")) {
                    Toast.makeText(AddInternActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keHomeActivity = new Intent(AddInternActivity.this, LoginActivity.class);
                    startActivity(keHomeActivity);
                    finish();
                } else {
                    Toast.makeText(AddInternActivity.this, jObj.getString("error_text"), Toast.LENGTH_SHORT).show();
                    namanewintern.setText(null);
                    umurnewintern.setText(null);
                    emailnewintern.setText(null);
                    alamatnewintern.setText(null);
                    telponnewintern.setText(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("nama", String.valueOf(namanewintern.getText()));
                params.put("umur", String.valueOf(umurnewintern.getText()));
                params.put("jenkel", gender);
                params.put("email", String.valueOf(emailnewintern.getText()));
                params.put("alamat", String.valueOf(alamatnewintern.getText()));
                params.put("telpon", String.valueOf(telponnewintern.getText()));
                params.put("performa", performanewintern.getSelectedItem().toString());
                params.put("project", String.valueOf(projectnewintern.getText()));
                params.put("action", action);

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }
    public void checkradiobutton() { // nilai dari variabel gender berdasarkan radio button yang dipilih
        if (jenkelnewintern.isChecked()){
            gender = "L";
        }
        else {
            gender = "P";
        }
    }

    public void initViews() {
        namanewintern = findViewById(R.id.ETNamaIntern);
        umurnewintern = findViewById(R.id.ETUmurIntern);
        jenkelnewintern = findViewById(R.id.RadBtnLaki);
        emailnewintern = findViewById(R.id.ETEmailIntern);
        alamatnewintern = findViewById(R.id.ETAlamatIntern);
        telponnewintern = findViewById(R.id.ETTelponIntern);
        clearbutton = findViewById(R.id.btnClearNewIntern);
        addbutton = findViewById(R.id.btnAddNewIntern);
        performanewintern = findViewById(R.id.ETPerformaIntern);
        projectnewintern = findViewById(R.id.ETProjectIntern);
    }

    public void toggleSpinnerPerformance() {
        String[] isiPerforma = {"-", "Buruk", "Cukup", "Baik", "Sangat Baik"};

        ArrayList<String> isiSpinner = new ArrayList<>(Arrays.asList(isiPerforma));

        // membuat adapter untuk mengisi dropdown
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, isiSpinner) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        performanewintern.setAdapter(spinnerAdapter);
    }
}
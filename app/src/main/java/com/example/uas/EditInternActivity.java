package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class EditInternActivity extends AppCompatActivity {
    EditText namaintern, umurintern, alamatintern, telponintern;
    TextView emailintern, jenkelintern;
    Button btnUpdate;
    String action = "edit";
    Bundle bundle;
    Spinner performaintern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_intern);
        // inisialisasi id di xml
        initViews();

        bundle = getIntent().getExtras();

        if (bundle != null) {
            // mengisi edit text dengan data intern
            inputEditTextByInternData();
        }

        btnUpdate.setOnClickListener(v -> editdata(bundle.getString("id"), bundle.getString("username")));
    }

    private void editdata(String id, String username) {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.interndata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(EditInternActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keEditActivity = new Intent(EditInternActivity.this, HomeActivity.class);
                    keEditActivity.putExtra("username", username);
                    startActivity(keEditActivity);
                    finish();
                } else {
                    Toast.makeText(EditInternActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nama", String.valueOf(namaintern.getText()));
                params.put("umur", String.valueOf(umurintern.getText()));
                params.put("alamat", String.valueOf(alamatintern.getText()));
                params.put("telpon", String.valueOf(telponintern.getText()));
                params.put("performa", performaintern.getSelectedItem().toString());
                params.put("action", action);

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    private void inputEditTextByInternData() {
        namaintern.setText(bundle.getString("nama"));
        umurintern.setText(bundle.getString("umur"));
        jenkelintern.setText(bundle.getString("jenkel"));
        emailintern.setText(bundle.getString("email"));
        alamatintern.setText(bundle.getString("alamat"));
        telponintern.setText(bundle.getString("telpon"));
        toggleSpinnerPerformance(bundle.getString("performa"));
    }

    void initViews() {
        namaintern = findViewById(R.id.ETDataNamaIntern);
        umurintern = findViewById(R.id.ETDataUmurIntern);
        jenkelintern = findViewById(R.id.TVDataJenkelIntern);
        emailintern = findViewById(R.id.TVDataEmailIntern);
        alamatintern = findViewById(R.id.ETDataAlamatIntern);
        telponintern = findViewById(R.id.ETDataTelponIntern);
        performaintern = findViewById(R.id.ETDataPerformaIntern);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    public void toggleSpinnerPerformance(String performa) {
        ArrayList<String> isiSpinner = new ArrayList<>(Arrays.asList("-", "Buruk", "Cukup", "Baik", "Sangat Baik"));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, isiSpinner) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        performaintern.setAdapter(spinnerAdapter);

        // mengecek apakah dropdown sebelumnya sudah diisi, jika sudah akan langsung menampilkan opsi dropdown-nya
        if (performa != null) {
            int spinnerPosition = spinnerAdapter.getPosition(performa);
            performaintern.setSelection(spinnerPosition);
        }
    }
}
package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.uas.Config.config;
import com.example.uas.Controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailUserActivity extends AppCompatActivity {
    TextView detailnama, detailumur, detailjenkel, detailemail, detailalamat, detailtelpon, detailperforma, detailproject;
    Bundle bundle;
    Button btnedit, btndelete;
    String action = "";
    String usernameFromHome, peran, firstName, nama;
    Toolbar toolbarDetail;
    LinearLayout llbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        //inisialisasi id di xml
        initViews();
        bundle = getIntent().getExtras();

        if (bundle != null) {
            nama = bundle.getString("nama");

            detailnama.setText(nama);
            detailumur.setText(bundle.getString("umur"));
            detailjenkel.setText(bundle.getString("jenkel"));
            detailemail.setText(bundle.getString("email"));
            detailalamat.setText(bundle.getString("alamat"));
            detailtelpon.setText(bundle.getString("telpon"));
            detailperforma.setText(bundle.getString("performa"));
            detailproject.setText(bundle.getString("project"));
            usernameFromHome = bundle.getString("username");
            peran = bundle.getString("peran");
        }

        // set toolbar dengan format "Profil 'nama intern'"
        firstName = nama.contains(" ") ? nama.split(" ")[0] : nama;
        toolbarDetail.setTitle("Profil " + firstName);

        //set visibility button untuk user
        if (peran.equals("admin")) {
            btnedit.setVisibility(View.VISIBLE);
            btndelete.setVisibility(View.VISIBLE);
        } else {
            llbtn.setVisibility(View.INVISIBLE);
        }

        btnedit.setOnClickListener(v -> {
            Intent keEditActivity = new Intent(DetailUserActivity.this, EditInternActivity.class);
            keEditActivity.putExtra("nama", bundle.getString("nama"));
            keEditActivity.putExtra("umur", bundle.getString("umur"));
            keEditActivity.putExtra("jenkel", bundle.getString("jenkel"));
            keEditActivity.putExtra("email", bundle.getString("email"));
            keEditActivity.putExtra("alamat", bundle.getString("alamat"));
            keEditActivity.putExtra("telpon", bundle.getString("telpon"));
            keEditActivity.putExtra("performa", bundle.getString("performa"));
            keEditActivity.putExtra("project", bundle.getString("project"));
            keEditActivity.putExtra("username", bundle.getString("username"));
            keEditActivity.putExtra("id", bundle.getString("id"));
            startActivity(keEditActivity);
        });

        btndelete.setOnClickListener(v -> {
            action = "delete";
            deletedata(bundle.getString("id"), usernameFromHome);
        });
    }

    private void deletedata(String id, String username) {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.interndata, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(DetailUserActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_SHORT).show();
                    Intent keHomeActivity = new Intent(DetailUserActivity.this, HomeActivity.class);
                    keHomeActivity.putExtra("username", username);
                    startActivity(keHomeActivity);
                    finish();
                } else {
                    Toast.makeText(DetailUserActivity.this, jsonObject.getString("error_text"), Toast.LENGTH_LONG).show();
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

                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    private void initViews() {
        btnedit = findViewById(R.id.btnEdit);
        btndelete = findViewById(R.id.btnDelete);

        detailnama = findViewById(R.id.TVDataNamaIntern);
        detailumur = findViewById(R.id.TVDataUmurIntern);
        detailjenkel = findViewById(R.id.TVDataJenkelIntern);
        detailemail = findViewById(R.id.TVDataEmailIntern);
        detailalamat = findViewById(R.id.TVDataAlamatIntern);
        detailtelpon = findViewById(R.id.TVDataTelponIntern);
        toolbarDetail = findViewById(R.id.toolbarDetail);
        detailperforma = findViewById(R.id.TVDataPerformaIntern);
        detailproject = findViewById(R.id.TVDataProjectIntern);
        llbtn = findViewById(R.id.LLBtn);
    }
}
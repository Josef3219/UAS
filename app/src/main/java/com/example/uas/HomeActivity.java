package com.example.uas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.uas.Config.config;
import com.example.uas.Controller.AppController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ArrayList<UserModel> data = new ArrayList<>();
    TextView greetings;
    TextView TVusername;
    Toolbar toolbar;
    FloatingActionButton floatingbuttonadd;
    String username, peran, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //inisialisasi id di xml
        initViews();
        Bundle bundle = getIntent().getExtras();

        recyclerView = findViewById(R.id.my_recycler_view);
        data = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        if (bundle != null) {
            username = bundle.getString("username");
            peran = bundle.getString("peran");
            fullname = bundle.getString("fullname");
            TVusername.setText(username);

            if (peran.equals("admin")) {
                floatingbuttonadd.show();
            } else {
                floatingbuttonadd.hide();
            }
        }

        floatingbuttonadd.setOnClickListener(v -> {
            Intent keAddInternActivity = new Intent(HomeActivity.this, AddInternActivity.class);
            startActivity(keAddInternActivity);
        });

        getData(username, peran, fullname);

        //mengatur klik item pada menu di toolbar
        toolbar.inflateMenu(R.menu.menu_home);
        toolbarClick(username, peran, fullname);

        toggleGreeting();
    }

    private void toggleGreeting() {
        // untuk menampilkan 'greeting' yang telah diatur berdasarkan jam 'real-time'
        // yang akan diupdate tiap detik berdasarkan hitungan waktu pada umumnya
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                runOnUiThread(() -> {
                    String greeting;
                    if(hour>= 12 && hour < 17){
                        greeting = "Good Afternoon";
                    } else if(hour >= 17 && hour < 19){
                        greeting = "Good Evening";
                    } else if(hour >= 19){
                        greeting = "Good Night";
                    } else {
                        greeting = "Good Morning";
                    }
                    greetings.setText(greeting+", ");
                });
            }
        }, 0, 1000);
    }

    private void initViews() {
        greetings = findViewById(R.id.TVGreetings);
        TVusername = findViewById(R.id.TVUsername);
        toolbar = findViewById(R.id.toolbar);
        floatingbuttonadd = findViewById(R.id.FABAdd);
    }

    private void getData(String username, String peran, String fullname) {
        StringRequest strReq = new StringRequest(Request.Method.POST, config.viewdataintern, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                JSONObject header = response.getJSONObject("data");

                Iterator<String> iterator = header.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject user = (JSONObject) header.get(key);

                    UserModel account = new UserModel();
                    account.setId(user.getString("id"));
                    account.setNama(user.getString("nama"));
                    account.setUmur(user.getString("umur"));
                    account.setJenkel(user.getString("jenkel"));
                    account.setEmail(user.getString("email"));
                    account.setAlamat(user.getString("alamat"));
                    account.setTelpon(user.getString("telpon"));
                    account.setGambar(user.getString("gambar"));
                    account.setPerforma(user.getString("performa"));
                    account.setProject(user.getString("project"));
                    data.add(account);
                }
                recyclerView.setAdapter(new UserListAdapter(this, data, username, peran, fullname));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->
            VolleyLog.d("Error: " + error.getMessage())){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("peran", peran);
                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    @SuppressLint("NonConstantResourceId")
    private void toolbarClick(String username, String peran, String fullname) {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_change_password:
                    Intent intentChange = new Intent(HomeActivity.this, ChangePasswordActivity.class);
                    intentChange.putExtra("username", username);
                    startActivity(intentChange);
                    break;
                case R.id.menu_project_list:
                    Intent intentProjectList = new Intent(HomeActivity.this, ProjectListActivity.class);
                    intentProjectList.putExtra("username", username);
                    intentProjectList.putExtra("peran", peran);
                    intentProjectList.putExtra("fullname", fullname);
                    startActivity(intentProjectList);
                    break;
                case R.id.menu_logout:
                    Intent intentLogOut = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intentLogOut);
                    finish();
                    break;
            }
            return false;
        });
    }
}
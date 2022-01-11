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

public class ProjectListActivity extends AppCompatActivity {
    Toolbar toolbar;
    Bundle bundle;
    String username, peran, fullname;
    TextView greetings, usernameGreet;
    FloatingActionButton floatingActionButton;
    ArrayList<ProjectModel> dataProject = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        // inisialisasi id di xml
        initViews();

        if (bundle != null){
            username = bundle.getString("username");
            peran = bundle.getString("peran");
            fullname = bundle.getString("fullname");
            usernameGreet.setText(username);

            if (peran.equals("admin")) {
                floatingActionButton.show();
            } else {
                floatingActionButton.hide();
            }
        }

        // mengatur isi dari recycler view
        toggleRecyclerView();

        // mengatur item klik pada menu di toolbar
        toolbar.inflateMenu(R.menu.menu_project_list);
        toolbarClick(username, peran);

        // mengatur 'greeting' di page daftar project
        toggleGreetings();

        floatingActionButton.setOnClickListener(v -> {
            Intent keAddProjectActivity = new Intent(ProjectListActivity.this, AddProjectActivity.class);
            keAddProjectActivity.putExtra("username", username);
            keAddProjectActivity.putExtra("peran", peran);
            keAddProjectActivity.putExtra("fullname", fullname);
            startActivity(keAddProjectActivity);
        });

        getData(username, peran, fullname);
    }


    private void getData(String username, String peran, String fullname) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.viewdataproject, responses -> {
            try {
                JSONObject response = new JSONObject(responses);
                JSONObject header = response.getJSONObject("data");

                Iterator<String> iterator = header.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JSONObject project = (JSONObject) header.get(key);

                    ProjectModel account = new ProjectModel();
                    account.setId(project.getString("id"));
                    account.setNamaproject(project.getString("namaproject"));
                    account.setPic(project.getString("pic"));
                    account.setStartdate(project.getString("startdate"));
                    account.setEnddate(project.getString("enddate"));
                    dataProject.add(account);
                }
                recyclerView.setAdapter(new ProjectListAdapter(dataProject, this, username, peran, fullname));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("Error:" + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("fullname", fullname);
                params.put("peran", peran);
                return params;
            }
        };
        AppController.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void toggleRecyclerView() {
        dataProject = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void toggleGreetings() {
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

    @SuppressLint("NonConstantResourceId")
    private void toolbarClick(String username, String peran) {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_intern_list_project:
                    Intent intentInternList = new Intent(ProjectListActivity.this, HomeActivity.class);
                    intentInternList.putExtra("username", username);
                    intentInternList.putExtra("peran", peran);
                    startActivity(intentInternList);
                    break;
                case R.id.menu_change_password_project:
                    Intent intentChange = new Intent(ProjectListActivity.this, ChangePasswordActivity.class);
                    intentChange.putExtra("username", username);
                    startActivity(intentChange);
                    break;
                case R.id.menu_logout_project:
                    Intent intentLogOut = new Intent(ProjectListActivity.this, LoginActivity.class);
                    startActivity(intentLogOut);
                    finish();
                    break;
            }
            return false;
        });
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarProject);
        bundle = getIntent().getExtras();
        usernameGreet = findViewById(R.id.TVUsernameProject);
        greetings = findViewById(R.id.TVGreetings);
        floatingActionButton = findViewById(R.id.FABAddProject);
        recyclerView = findViewById(R.id.my_recycler_view_project);
    }
}
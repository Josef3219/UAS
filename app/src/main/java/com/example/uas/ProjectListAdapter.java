package com.example.uas;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
    private final ArrayList<ProjectModel> dataProject;
    String username, peran, fullname;
    Context context;
    // constructor
    public ProjectListAdapter(ArrayList<ProjectModel> dataProject, Context context, String username, String peran, String fullname) {
        this.dataProject = dataProject;
        this.username = username;
        this.peran = peran;
        this.context = context;
        this.fullname = fullname;
    }

    @NonNull
    @Override
    //menghubungkan ke View untuk masing-masing item
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // memberi nilai ke item-item untuk recycler view
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textHead = holder.textTitle;
        TextView textSubHead = holder.textSubtitle;

        textHead.setText(dataProject.get(position).getNamaproject());
        textSubHead.setText(dataProject.get(position).getPic());

        holder.itemView.setOnClickListener(view -> {
            Intent keDetailProjectActivity = new Intent(context, DetailProjectActivity.class);
            putExtraUserDataItem(dataProject, keDetailProjectActivity, position);
            context.startActivity(keDetailProjectActivity);
        });
    }

    @Override
    // menghitung jumlah item yang ditampilkan di recycler view
    public int getItemCount() {
        return dataProject.size();
    }

    // inisialisasi id di item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSubtitle;
        public ViewHolder(@NonNull View v) {
            super(v);
            textTitle = v.findViewById(R.id.TVNamaProject);
            textSubtitle = v.findViewById(R.id.TVPICProject);
        }
    }

    // membawa nilai-nilai berikut ke halaman yang dituju
    private void putExtraUserDataItem(ArrayList<ProjectModel> dataItem, Intent intentParam, int position) {
        intentParam.putExtra("id", dataItem.get(position).getId());
        intentParam.putExtra("namaproject", dataItem.get(position).getNamaproject());
        intentParam.putExtra("pic", dataItem.get(position).getPic());
        intentParam.putExtra("startdate", dataItem.get(position).getStartdate());
        intentParam.putExtra("enddate", dataItem.get(position).getEnddate());
        intentParam.putExtra("username", this.username);
        intentParam.putExtra("peran", this.peran);
        intentParam.putExtra("fullname", this.fullname);
    }
}
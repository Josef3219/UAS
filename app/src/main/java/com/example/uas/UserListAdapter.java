package com.example.uas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    private final ArrayList<UserModel> dataUser;
    String username, peran, fullname;
    Context context;
    //constructor
    public UserListAdapter(Context context, ArrayList<UserModel> dataItem, String username, String peran, String fullname) {
        this.context = context;
        this.dataUser = dataItem;
        this.username = username;
        this.peran = peran;
        this.fullname = fullname;
    }

    @NonNull
    @Override
    //menghubungkan ke View untuk masing-masing item
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intern, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // mengisi nilai ke item-item untuk recycler view
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView icon = holder.gambaricon;
        TextView textHead = holder.textTitle;
        TextView textSubHead = holder.textSubtitle;

        Glide.with(holder.itemView.getContext())
                .load(dataUser.get(position).getGambar())
                .into(icon);
        textHead.setText(dataUser.get(position).getNama());
        textSubHead.setText(dataUser.get(position).getTelpon());

        holder.itemView.setOnClickListener(view -> {
            Intent keDetailUserActivity = new Intent(context, DetailUserActivity.class);
            putExtraUserDataItem(dataUser, keDetailUserActivity, position);
            context.startActivity(keDetailUserActivity);
        });
    }

    @Override
    // menghitung jumlah item yang ditampilkan di recycler view
    public int getItemCount() {
        return dataUser.size();
    }

    // inisialisasi id di item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gambaricon;
        TextView textTitle;
        TextView textSubtitle;

        public ViewHolder(@NonNull View v) {
            super(v);
            textTitle = v.findViewById(R.id.TVNamaUser);
            textSubtitle = v.findViewById(R.id.TVTelponUser);
            gambaricon = v.findViewById(R.id.IVGambarUser);
        }
    }
    // membawa nilai-nilai berikut ke halaman yang dituju
    private void putExtraUserDataItem(ArrayList<UserModel> dataItem, Intent intentParam, int position) {
        intentParam.putExtra("username", this.username);
        intentParam.putExtra("peran", this.peran);
        intentParam.putExtra("fullname", this.fullname);
        intentParam.putExtra("id", dataItem.get(position).getId());
        intentParam.putExtra("nama", dataItem.get(position).getNama());
        intentParam.putExtra("umur", dataItem.get(position).getUmur());
        intentParam.putExtra("jenkel", dataItem.get(position).getJenkel());
        intentParam.putExtra("email", dataItem.get(position).getEmail());
        intentParam.putExtra("alamat", dataItem.get(position).getAlamat());
        intentParam.putExtra("telpon", dataItem.get(position).getTelpon());
        intentParam.putExtra("performa", dataItem.get(position).getPerforma());
        intentParam.putExtra("project", dataItem.get(position).getProject());
    }
}

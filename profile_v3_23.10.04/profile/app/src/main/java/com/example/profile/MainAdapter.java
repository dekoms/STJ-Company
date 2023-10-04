package com.example.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
    private ArrayList<MainData> arrayList;
    private ArrayList<Pet> petArrayList;
    private Context context;

    public MainAdapter(ArrayList<MainData> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.CustomViewHolder holder, int position) {
        holder.ivProfile.setImageURI(arrayList.get(position).getIvProfileUri());
        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvAge.setText(arrayList.get(position).getAge());
        holder.tvGender.setText(arrayList.get(position).getGender());
        holder.tvBirth.setText(arrayList.get(position).getBirth());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
        catch(IndexOutOfBoundsException ex) {
            ex.printStackTrace();

        }
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivProfile;
        protected TextView tvName;
        protected TextView tvAge;
        protected TextView tvGender;
        protected TextView tvBirth;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivProfile = itemView.findViewById(R.id.iv_profile);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvAge = itemView.findViewById(R.id.tv_age);
            this.tvGender = itemView.findViewById(R.id.tv_gender);
            this.tvBirth = itemView.findViewById(R.id.tv_birth);
        }
    }
}
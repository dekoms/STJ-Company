package com.example.merge;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.CustomViewHolder> {
    private ArrayList<Pet> arrayList;
    private Context context;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;



    public ProfileAdapter(ArrayList<Pet> arrayList) {

        this.context = context;
        this.arrayList = arrayList;

    }


    @NonNull
    @Override
    public ProfileAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.CustomViewHolder holder, int position) {
        final Pet pet = arrayList.get(position);
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .circleCrop()
                .into(holder.ivProfile);
        //holder.ivProfile.setImageURI(arrayList.get(position).getIvProfileUri());
        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvAge.setText(arrayList.get(position).getAge());
        holder.tvGender.setText(arrayList.get(position).getGender());
        holder.tvBirth.setText(arrayList.get(position).getBirth());



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseDatabase mDatabase;
                DatabaseReference dataRef;
                mFirebaseStorage = FirebaseStorage.getInstance();
                mStorageReference = mFirebaseStorage.getReference();

                String pname = arrayList.get(holder.getAdapterPosition()).getName();

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                mDatabase = FirebaseDatabase.getInstance();
                dataRef = mDatabase.getReference().child("profile").child("pet").child(uid).child(pname);

                StorageReference imageReference = mStorageReference.child("images/" + uid +"/"+ "petimage/" + pname + ".jpg");

                imageReference.delete();

                dataRef.removeValue();

                remove(holder.getAdapterPosition());
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int mPostion = holder.getAdapterPosition();

                Context context = view.getContext();

                Intent intent;
                intent = new Intent(context, SubProfileActivity.class);

                intent.putExtra("tvimage", arrayList.get(mPostion).getImage());
                intent.putExtra("tvname", arrayList.get(mPostion).getName());
                intent.putExtra("tvage", arrayList.get(mPostion).getAge());
                intent.putExtra("tvbirth", arrayList.get(mPostion).getBirth());
                intent.putExtra("tvgender", arrayList.get(mPostion).getGender());
                ((MainActivity)context).startActivity(intent);
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
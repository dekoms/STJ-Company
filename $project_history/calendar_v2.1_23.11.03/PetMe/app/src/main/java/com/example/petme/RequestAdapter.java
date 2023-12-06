package com.example.petme;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    List<FriendRequest> requestList;
    OnItemClickListener listener;

    DatabaseReference mDatabaseReference;
    String name;

    public RequestAdapter(List<FriendRequest> requestList, OnItemClickListener listener){
        this.requestList = requestList;
        this.listener = listener;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, int position) {
        FriendRequest friendRequest = requestList.get(position);

        mDatabaseReference.child("UserAccount").child(friendRequest.getSenderUserId()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
                holder.friendname.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RequestAdapter", String.valueOf(error.toException()));
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAcceptClick(friendRequest);
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRejectClick(friendRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView friendname;
        Button accept, reject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            friendname = itemView.findViewById(R.id.name_view);
            accept = itemView.findViewById(R.id.accept_button);
            reject = itemView.findViewById(R.id.reject_button);
        }
    }

    public interface OnItemClickListener {
        void onAcceptClick(FriendRequest friendRequest);
        void onRejectClick(FriendRequest friendRequest);
    }

}
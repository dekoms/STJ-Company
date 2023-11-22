package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

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
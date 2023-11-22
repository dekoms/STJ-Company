package com.example.petme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestAdapter adapter;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mFirebaseAuth;
    RequestManager manager;
    Button back;

    static List<FriendRequest> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        recyclerView = findViewById(R.id.requestRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        manager = new RequestManager();

        back = findViewById(R.id.Back);

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList, new RequestAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(FriendRequest friendRequest) {
                String ID = friendRequest.getSenderUserId();
                mDatabaseReference.child("UserAccount").child(friendRequest.getSenderUserId()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String friendname = snapshot.getValue(String.class);

                        FriendList.friendList.add(friendname);
                        mDatabaseReference.child("FriendList").child(mFirebaseAuth.getCurrentUser().getUid()).child(ID).setValue(friendname);

                        manager.AcceptRequest(friendRequest.getRequestId());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDatabaseReference.child("UserAccount").child(mFirebaseAuth.getCurrentUser().getUid()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.getValue(String.class);
                        mDatabaseReference.child("FriendList").child(ID).child(mFirebaseAuth.getCurrentUser().getUid()).setValue(username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                manager.AcceptRequest(friendRequest.getRequestId());
                adapter.notifyDataSetChanged();
                FriendList.adapter.notifyDataSetChanged();

                requestList.remove(friendRequest);
            }

            @Override
            public void onRejectClick(FriendRequest friendRequest) {
                manager.RejectRequest(friendRequest.getRequestId());
                adapter.notifyDataSetChanged();

                requestList.remove(friendRequest);
            }
        });

        recyclerView.setAdapter(adapter);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mFirebaseAuth = FirebaseAuth.getInstance();

        String userID = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference.child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot dataSnapshot : snapshot.getChildren() ){

                    if(userID.equals(dataSnapshot.child("receiverUserId").getValue().toString()) && dataSnapshot.child("status").getValue(String.class).equals("pending")){
                        FriendRequest request = dataSnapshot.getValue(FriendRequest.class);
                        requestList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RequestActivity", String.valueOf(error.toException()));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
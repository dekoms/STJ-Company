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
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class FriendsFeed extends AppCompatActivity {

    static String ID;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;
    private RecyclerView recyclerView;
    static FriendsFeedAdapter friendsfeedAdapter;

    static List<FeedItem> feedList = new ArrayList<>();

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_feed);

        recyclerView = findViewById(R.id.recyclerView);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");

        Intent intent = getIntent();

        if(intent != null){
            ID = intent.getStringExtra("targetID");

            setList();

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            friendsfeedAdapter = new FriendsFeedAdapter(feedList, this);
            recyclerView.setAdapter(friendsfeedAdapter);

            back = findViewById(R.id.Back);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedList.clear();
                    finish();
                }
            });
        }
    }

    void setList(){
        mDatabaseReference.child("Feed").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot user : snapshot.getChildren() ){
                    FeedItem newItem = user.getValue(FeedItem.class);

                    feedList.add(newItem);
                }

                Feed.sortByDateDescending(feedList);

                friendsfeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FriendsFeed", String.valueOf(error.toException()));
            }
        });
    }
}
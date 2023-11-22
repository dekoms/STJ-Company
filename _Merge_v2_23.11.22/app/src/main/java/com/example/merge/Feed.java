package com.example.merge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;

import java.util.*;

public class Feed extends Fragment {

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;
    private RecyclerView recyclerView;
    static FeedAdapter feedAdapter;

    static List<FeedItem> feedList = new ArrayList<>();

    Button add;

    public Feed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        add = rootView.findViewById(R.id.Add);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");

        if(!MainActivity.FeedAccess) setList();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedAddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        feedAdapter = new FeedAdapter(feedList, getActivity());
        recyclerView.setAdapter(feedAdapter);

        return rootView;
    }

    public static void sortByDateDescending(List<FeedItem> feedItemList) {
        Collections.sort(feedItemList, new Comparator<FeedItem>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            @Override
            public int compare(FeedItem item1, FeedItem item2) {
                try {
                    Date date1 = dateFormat.parse(item1.getDate());
                    Date date2 = dateFormat.parse(item2.getDate());

                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }

    void setList(){
        mDatabaseReference.child("Feed").child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot user : snapshot.getChildren() ){
                    FeedItem newItem = user.getValue(FeedItem.class);

                    feedList.add(newItem);
                }

                sortByDateDescending(feedList);

                feedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Feed", String.valueOf(error.toException()));
            }


        });

        MainActivity.FeedAccess = true;
    }
}

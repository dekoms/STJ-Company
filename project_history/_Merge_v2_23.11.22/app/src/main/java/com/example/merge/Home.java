package com.example.merge;

import android.app.Activity;
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

public class Home extends Fragment{
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe")
            .child("UserAccount").child(firebaseUser.getUid()).child("name");
    String name;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button logout, setting;
    private RecyclerView recyclerView;
    private ArrayList<todoItem> list = new ArrayList<>();
    todoAdapter adapter;
    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        logout = rootView.findViewById(R.id.settingButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = requireActivity().getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                Log.d("2",sharedPreferences.getString("id",""));
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                Log.d("MyTag", "로그아웃 성공");
            }
        });
        TextView time = rootView.findViewById(R.id.textView);
        time.setText(getTime()); // 현재 날짜 가져옴
        recyclerView = rootView.findViewById(R.id.recyclerView2);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PetMe")
                .child("Todo").child(getTime());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(Objects.equals(dataSnapshot.getKey(), name)){
                        DatabaseReference ref1 = databaseReference.child(name);
                        ref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    list.add(new todoItem(dataSnapshot1.child("title").getValue(String.class),
                                            dataSnapshot1.child("startTime").getValue(String.class),
                                            dataSnapshot1.child("endTime").getValue(String.class),
                                            dataSnapshot1.child("memo").getValue(String.class)));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new todoAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        setting = rootView.findViewById(R.id.profile_btn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인텐트로 ProfileActivity 실행
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    public String getTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        return simpleDateFormat.format(date);
    }
}

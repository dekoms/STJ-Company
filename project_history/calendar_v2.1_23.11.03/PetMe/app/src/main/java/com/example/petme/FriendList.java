package com.example.petme;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends Fragment{

    Button addFriendButton, deleteFriendButton, requestButton;
    static List<String> friendList;
    static FriendListAdapter adapter;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    public FriendList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_list, container, false);

        RecyclerView friendRecyclerView = rootView.findViewById(R.id.friendRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        friendRecyclerView.setLayoutManager(layoutManager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mFirebaseAuth = FirebaseAuth.getInstance();

        friendList = new ArrayList<>(); // 친구 목록 데이터를 가져와서 채워넣어야 함

        adapter = new FriendListAdapter(friendList);
        friendRecyclerView.setAdapter(adapter);

        setList();

        addFriendButton = rootView.findViewById(R.id.addFriendButton);
        deleteFriendButton = rootView.findViewById(R.id.deleteFriendButton);
        requestButton = rootView.findViewById(R.id.RequestButton);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RequestActivity.class);
                startActivity(intent);
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("이메일 검색");
                // Email 을 통한 사용자 검색 알람 활성화

                final EditText inputEditText = new EditText(requireContext());
                builder.setView(inputEditText);

                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    // 추가 버튼 클릭시 이메일 검색
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newFriendID = inputEditText.getText().toString();
                        if (!newFriendID.isEmpty()) { // 검색창이 비워저 있지 않을 경우
                            new SearchForEmail(newFriendID, mDatabaseReference, getContext());
                        }
                        else{
                            Toast.makeText(v.getContext(), "email을 입력하세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create().show();
            }
        });

        deleteFriendButton.setOnClickListener(new View.OnClickListener() {  // 선택한 친구 삭제
            @Override
            public void onClick(View v) {
                int selectedPosition = adapter.getSelectedPosition();
                String name = adapter.getSelectedString(selectedPosition);


                if (selectedPosition != RecyclerView.NO_POSITION) {
                    friendList.remove(selectedPosition);

                    mDatabaseReference.child("FriendList").child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for( DataSnapshot user : snapshot.getChildren() ){
                                if(name.equals(user.getValue(String.class))){
                                    String targetID = user.getKey();
                                    mDatabaseReference.child("FriendList").child(mFirebaseAuth.getCurrentUser().getUid()).child(user.getKey()).removeValue();

                                    mDatabaseReference.child("FriendList").child(targetID).child(mFirebaseAuth.getCurrentUser().getUid()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    adapter.notifyItemRemoved(selectedPosition);
                }
                else{
                    Toast.makeText(v.getContext(), "친구를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void setList(){
        mDatabaseReference.child("FriendList").child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot user : snapshot.getChildren() ){
                    String name = user.getValue(String.class);
                    friendList.add(name);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FriendList", String.valueOf(error.toException()));
            }
        });
    }
}

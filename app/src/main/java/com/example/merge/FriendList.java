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

import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.text.SimpleDateFormat;
import java.util.*;

import java.util.*;

public class FriendList extends Fragment{

    Button addFriendButton, deleteFriendButton, playButton;

    static Button requestButton;
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

        friendList = new ArrayList<>();

        adapter = new FriendListAdapter(friendList);
        friendRecyclerView.setAdapter(adapter);

        setList();

        addFriendButton = rootView.findViewById(R.id.addFriendButton);
        deleteFriendButton = rootView.findViewById(R.id.deleteFriendButton);
        requestButton = rootView.findViewById(R.id.RequestButton);
        playButton = rootView.findViewById(R.id.Play);

        CheckRequest();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectPosition = adapter.getSelectedPosition();
                String name = adapter.getSelectedString(selectPosition);

                if(selectPosition != RecyclerView.NO_POSITION){

                    mDatabaseReference.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserAccount target;

                            for(DataSnapshot data : snapshot.getChildren()){
                                target = data.getValue(UserAccount.class);
                                if(name.equals(target.getName())){
                                    String ID = target.getIdTokken();

                                    Intent intent = new Intent(getContext(), FriendsFeed.class);
                                    intent.putExtra("targetID", ID);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(view.getContext(), "친구를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                if (selectedPosition != RecyclerView.NO_POSITION) {
                    String name = adapter.getSelectedString(selectedPosition);
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


    private void CheckRequest(){
        String userID = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference.child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot data : snapshot.getChildren()){
                    if(userID.equals(data.child("receiverUserId").getValue().toString()) && data.child("status").getValue(String.class).equals("pending")) count++;
                }

                if(count != 0){
                    requestButton.setText("친구 요청 (" + count + ")");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

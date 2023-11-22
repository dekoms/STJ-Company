package com.example.petme;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchForEmail {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    boolean result = false;
    String name;
    String ID;

    SearchForEmail(String newFriendID, DatabaseReference mRef, Context context){

        mFirebaseAuth = FirebaseAuth.getInstance();
        this.mDatabaseReference = mRef;

        mDatabaseReference.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Firebase DB 를 받아 오는 곳
                for( DataSnapshot userSnapshot : snapshot.getChildren() ){
                    // DB 의 UserAccount 의 하위 계정들을 모두 탐색
                    if(newFriendID.equals(userSnapshot.child("emailID").getValue(String.class)) == true) {
                        // 검색한 email과 일치하는 계정을 발견할 경우 친구 추가 기능 시작
                        name = userSnapshot.child("name").getValue(String.class);
                        ID = userSnapshot.getKey();

                        RequestManager requestmanager = new RequestManager();

                        requestmanager.SendRequest(ID);
                        Toast.makeText(context, "친구요청 전송!", Toast.LENGTH_SHORT).show();

                        /*
                        FriendList.friendList.add(name);

                        mDatabaseReference.child("FriendList").child(mFirebaseAuth.getCurrentUser().getUid()).child(ID).setValue(name);

                        FriendList.adapter.notifyDataSetChanged();
                        */
                        result = true;

                        break;
                    }
                }
                if(result == false) Toast.makeText(context, "검색 실패!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchForEmail", String.valueOf(error.toException()));
            }
        });
    }
}

package com.example.petme;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestManager {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    public RequestManager(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void SendRequest(String receiverID){
        String requestID = mDatabaseReference.child("Request").push().getKey(); // Request Table 에 request를 추가하고 그 키값 가져오기
        String senderID = mFirebaseAuth.getCurrentUser().getUid(); // 현재 접속한 사용자의 ID값 가져오기

        FriendRequest newRequest = new FriendRequest(requestID, senderID, receiverID, "pending");
        // 위 정보를 바탕으로 새로운 요청 메세지 생성

        mDatabaseReference.child("Request").child(requestID).setValue(newRequest);
        // Request Table 에 정보 추가
    }

    public void AcceptRequest(String requestID){
        mDatabaseReference.child("Request").child(requestID).child("status").setValue("accept");
    }

    public void RejectRequest(String requestID){
        mDatabaseReference.child("Request").child(requestID).child("status").setValue("reject");
    }

}

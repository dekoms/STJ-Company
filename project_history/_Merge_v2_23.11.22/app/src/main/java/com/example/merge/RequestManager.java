package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.*;

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

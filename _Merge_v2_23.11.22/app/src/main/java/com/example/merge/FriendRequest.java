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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.*;

public class FriendRequest {

    private String requestId;
    private String senderUserId;
    private String receiverUserId;
    private String status;

    public FriendRequest() { }

    public FriendRequest(String requestID, String senderUserId, String receiverUserId, String status){
        this.requestId = requestID;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

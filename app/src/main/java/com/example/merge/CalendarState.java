package com.example.merge;

import static com.example.merge.CalendarUtil.*;
import static com.example.merge.CalendarFragment.*;

import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;

import java.util.HashSet;

public class CalendarState {
    DatabaseReference databaseReference;
    String displayDate;
    HashSet<String> stringDateList;
    LayoutInflater inflater;

    public CalendarState(LayoutInflater inflater, DatabaseReference databaseReference, String displayDate, HashSet<String> stringDateList) {
        this.inflater = inflater;
        this.databaseReference = databaseReference;
        this.displayDate = displayDate;
        this.stringDateList = stringDateList;
    }

    public void createState(){


        try {
            databaseReference.child("TodoCell").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot children: snapshot.getChildren()){
                        stringDateList.add(children.getKey());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //디비를 가져오는 중 에러 발생 시
                    Log.e("FirebaseTodoCell", String.valueOf(error.toException()));
                }
            });
        } catch (NullPointerException e){
            Log.e("HashSet", "HashSet 크기: "+String.valueOf(stringDateList.size()));
        }

        for(String i:stringDateList)
            Log.e("safc", i);
    }
}

package com.example.merge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.todoViewHolder> {
    final List<TodoItem> itemList;
    private Context context;
    String name;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PetMe")
            .child("Todo").child(Home.getTime());
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe")
            .child("UserAccount").child(firebaseUser.getUid()).child("name");
    public TodoAdapter(Context context, List<TodoItem> itemList){
        this.context = context;
        this.itemList = itemList;

    }
    @NonNull
    @Override
    public TodoAdapter.todoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_view, parent,false);
        return new TodoAdapter.todoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.todoViewHolder holder, int position) {
        holder.checkBox.setText(itemList.get(position).getTodo());
        holder.todoMemo.setText(itemList.get(position).getMemo());
        if(isAllday(position)){
            holder.todoStartTime.setText("하루 종일");
            holder.todoEndTime.setText("하루 종일");
        } else {
            holder.todoStartTime.setText(itemList.get(position).getStartTime());
            holder.todoEndTime.setText(itemList.get(position).getEndTime());
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.getValue(String.class);
                        DatabaseReference mDataReference = databaseReference.child(name);
                        if(b) mDataReference.child(holder.checkBox.getText().toString()).child("done").setValue(true);
                        else mDataReference.child(holder.checkBox.getText().toString()).child("done").setValue(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class todoViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public TextView todoStartTime, todoMemo, startTime, memoView, endTime, todoEndTime;
        public todoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            startTime = itemView.findViewById(R.id.startTime);
            todoStartTime = itemView.findViewById(R.id.todoStartTime);
            memoView = itemView.findViewById(R.id.memoView);
            todoMemo = itemView.findViewById(R.id.todoMemo);
            endTime = itemView.findViewById(R.id.endTime);
            todoEndTime = itemView.findViewById(R.id.todoEndTime);
        }
    }
    boolean isAllday(int position){
        return itemList.get(position).getStartTime().equals("") && itemList.get(position).getEndTime().equals("");
    }

}
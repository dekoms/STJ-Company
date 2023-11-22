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

import java.text.SimpleDateFormat;
import java.util.*;

public class todoAdapter extends RecyclerView.Adapter<todoAdapter.todoViewHolder> {
    private final List<todoItem> itemList;
    private Context context;
    public todoAdapter(Context context, List<todoItem> itemList){
        this.context = context;
        this.itemList = itemList;

    }
    @NonNull
    @Override
    public todoAdapter.todoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_view, parent,false);
        return new todoAdapter.todoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull todoAdapter.todoViewHolder holder, int position) {
        todoItem todoItem = itemList.get(position);

        String test = todoItem.getTodo();
        holder.checkBox.setText(itemList.get(position).getTodo());
        holder.todoStartTime.setText(itemList.get(position).getStartTime());
        holder.todoMemo.setText(itemList.get(position).getMemo());
        holder.todoEndTime.setText(itemList.get(position).getEndTime());
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
}

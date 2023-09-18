package com.example.customcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    Context mContext;
    ArrayList<TodoListItem> list;

    public TodoListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.one_click, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoListItem item = list.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(TodoListItem item){
        list.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time_tv;
        TextView content_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time_tv = itemView.findViewById(R.id.time_tv);
            content_tv = itemView.findViewById(R.id.content_tv);
        }

        public void setItem(TodoListItem item){
            time_tv.setText(item.time);
            content_tv.setText(item.content);
        }
    }

}

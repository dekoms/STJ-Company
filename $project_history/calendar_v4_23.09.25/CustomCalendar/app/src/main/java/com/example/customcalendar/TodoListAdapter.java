package com.example.customcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    Context mContext;
    ArrayList<TodoListItem> list;

    public TodoListAdapter(Context mContext, ArrayList<TodoListItem> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.todo_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoListItem item = list.get(position);

        //사용자 정의 함수
        holder.setItem(item);

        //디비 각 변수에 데이터 저장
        //title, content 값 getText()

        //이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = holder.getAdapterPosition();  //click position

                TodoListItem todoListItem = list.get(currentPos);

                Toast.makeText(mContext, todoListItem.content, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int currentPos = holder.getAdapterPosition();  //click position

                deleteItem(currentPos);

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(TodoListItem item){
        list.add(item);
        //이게 문제인가??
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        try{
            list.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView calendar_iv;
        TextView time_tv;
        TextView content_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.calendar_iv = itemView.findViewById(R.id.calendar_image);
            this.time_tv = itemView.findViewById(R.id.time_tv);
            this.content_tv = itemView.findViewById(R.id.content_tv);

        }

        public void setItem(TodoListItem item){
            calendar_iv.setImageResource(item.resID);
            time_tv.setText(item.time);
            content_tv.setText(item.content);
        }
    }

}

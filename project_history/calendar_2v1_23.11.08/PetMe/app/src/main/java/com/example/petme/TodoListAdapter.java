package com.example.petme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    ArrayList<TodoListItem> todoList;

    public TodoListAdapter(ArrayList<TodoListItem> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.todo_list_item, parent, false);

        return new TodoListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        TodoListItem item = todoList.get(position);

        //사용자 정의 함수
        holder.setItem(item);

        //이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = holder.getAdapterPosition();  //click position

                TodoListItem todoListItem = todoList.get(currentPos);

                Toast.makeText(view.getContext(), todoListItem.title, Toast.LENGTH_SHORT).show();
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
        return todoList.size();
    }

    public void addItem(TodoListItem item){
        todoList.add(item);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        try{
            todoList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    static class TodoListViewHolder extends RecyclerView.ViewHolder{
        TextView tv_time_summary;
        TextView tv_title;
        TextView tv_time;

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_time_summary = itemView.findViewById(R.id.tv_time_summary);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_time = itemView.findViewById(R.id.tv_time);
        }

        public void setItem(TodoListItem item){
            tv_time_summary.setText(item.allDay ? "🕛" : item.startTime);
            tv_title.setText(item.title);
            tv_time.setText(item.allDay ? "하루 종일" : (item.startTime + " - " + item.endTime));
        }
    }
}

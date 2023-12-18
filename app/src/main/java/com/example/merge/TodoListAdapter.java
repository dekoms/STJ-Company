package com.example.merge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    private ArrayList<TodoListItem> todoList;
    private DatabaseReference databaseReference;

    public TodoListAdapter(ArrayList<TodoListItem> todoList, DatabaseReference databaseReference) {
        this.todoList = todoList;
        this.databaseReference = databaseReference;;
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

                //서버 데이터 처리
                holder.updateDetailedTodo(currentPos);
                updateItem(currentPos, item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int currentPos = holder.getAdapterPosition();  //click position

                holder.deleteDetailedTodo(currentPos);
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

    public void updateItem(int position, TodoListItem item){
        todoList.set(position, item);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        todoList.remove(position);
        notifyItemRemoved(position);
    }


    class TodoListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_time_summary;
        private TextView tv_title;
        private TextView tv_time;

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_time_summary = itemView.findViewById(R.id.tv_time_summary);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_time = itemView.findViewById(R.id.tv_time);
        }

        public void setItem(TodoListItem item){
            tv_time_summary.setText(item.isAllDay() ? "   🕛   " : item.getStartTime());
            tv_title.setText(item.getTitle());
            tv_time.setText(item.isAllDay() ? "하루 종일" : (item.getStartTime() + " - " + item.getEndTime()));
        }

        public void updateDetailedTodo(int position){
            ViewGroup detailedTodoDialogViewGroup = (ViewGroup) tv_time_summary.getParent().getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(detailedTodoDialogViewGroup.getContext());

            DetailedTodoDialog detailedTodoDialog = new DetailedTodoDialog(inflater, todoList, databaseReference, position);
            detailedTodoDialog.update();
        }

        public void deleteDetailedTodo(int position){
            ViewGroup detailedTodoDialogViewGroup = (ViewGroup) tv_time_summary.getParent().getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(detailedTodoDialogViewGroup.getContext());

            CalendarDialog calendarDialog = new CalendarDialog(inflater, todoList, databaseReference, position);
            calendarDialog.delete();
        }
    }
}

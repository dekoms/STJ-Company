package com.example.softwarecalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Todo> list;
    int layout;

    public TodoAdapter(Context context, ArrayList<Todo> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Todo getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout, viewGroup,false);

            holder = new ViewHolder();
            holder.txtTime = view.findViewById(R.id.txtTimeItem);
            holder.txtContent = view.findViewById(R.id.txtContentItem);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        Todo todo = list.get(i);
        holder.txtTime.setText(todo.time);
        holder.txtContent.setText(todo.content);

        return view;
    }
    class ViewHolder{
        TextView txtTime;
        TextView txtContent;
    }
}

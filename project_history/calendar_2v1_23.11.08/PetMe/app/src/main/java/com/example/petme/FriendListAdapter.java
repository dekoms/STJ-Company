package com.example.petme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private List<String> friendList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public FriendListAdapter(List<String> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        String friendName = friendList.get(position);
        holder.friendNameTextView.setText(friendName);

        if (selectedPosition == holder.getAdapterPosition()) {
            //holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.highlight));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedString(int selectedPosition) { return friendList.get(selectedPosition); }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendNameTextView;

        FriendViewHolder(View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.friendNameTextView);
        }
    }
}

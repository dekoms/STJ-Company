package com.example.petme;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<FeedItem> feedList;
    private Context context;

    public FeedAdapter(List<FeedItem> feedList, Context context) {
        this.feedList = feedList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedItem feedItem = feedList.get(position);

        Pair<String, String> currentYearDate = getCurrentYearDate();

        holder.TextView.setText("등록일 : "+ currentYearDate.second);
        holder.mediaView.setImageResource(feedItem.getMediaResourceId());

        holder.mediaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registrationDate = currentYearDate.second;
                int mediaResourceId = feedItem.getMediaResourceId();
                showFeedDetails(registrationDate, mediaResourceId, feedItem.getCaption());
            }
        });
    }

    private void showFeedDetails(String registrationDate, int mediaResourceId, String caption) {
        // Intent를 사용하여 새 액티비티를 호출하고, 사용자가 작성한 글을 전달
        Intent intent = new Intent(context, FeedDetailsActivity.class);
        intent.putExtra("registrationDate", registrationDate);
        intent.putExtra("mediaResourceId", mediaResourceId);
        intent.putExtra("caption", caption);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public TextView TextView;
        public ImageView mediaView;
        public LinearLayout feedLayout;

        public FeedViewHolder(View itemView) {
            super(itemView);

            // 뷰홀더에 있는 뷰들을 findViewById를 통해 참조.
            TextView = itemView.findViewById(R.id.TextView);
            mediaView = itemView.findViewById(R.id.mediaView);
            feedLayout = itemView.findViewById(R.id.feedLayout);
        }
    }

    Pair<String, String> getCurrentYearDate(){
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date = new Date();
        String currentYear = dateFormatYear.format(date);
        String currentDate = dateFormatDate.format(date);

        return new Pair<>(currentYear, currentDate);

    }

}

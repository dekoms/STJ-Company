package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.util.*;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;

    private List<FeedItem> feedList;
    private Context context;

    static int selectedPosition = RecyclerView.NO_POSITION;

    public FeedAdapter(List<FeedItem> feedList, Context context) {
        this.feedList = feedList;
        this.context = context;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mStorageReference = FirebaseStorage.getInstance().getReference("PetMe");
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

        String currentYearDate = feedItem.getDate();

        holder.TextView.setText("등록일 : "+ currentYearDate);


        if(feedItem.getImageURL() == null){
            holder.mediaView.setImageResource(R.drawable.null_image);
        }
        else{
            StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(feedItem.getImageURL());

            image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url = uri.toString();

                    Glide.with(context)
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .fitCenter()
                            .into(holder.mediaView);
                }
            });
        }

        holder.mediaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                String registrationDate = currentYearDate;
                String ImageURL;

                if(feedItem.getImageURL() == null) ImageURL = null;
                else ImageURL = feedItem.getImageURL();

                showFeedDetails(registrationDate, ImageURL, feedItem.getCaption(), feedItem.getTokenID());
            }
        });
    }

    private void showFeedDetails(String registrationDate, String ImageURL, String caption, String tokenID) {
        // Intent를 사용하여 새 액티비티를 호출하고, 사용자가 작성한 글을 전달
        Intent intent = new Intent(context, FeedDetailsActivity.class);
        intent.putExtra("registrationDate", registrationDate);
        intent.putExtra("ImageURL", ImageURL);
        intent.putExtra("caption", caption);
        intent.putExtra("tokenID", tokenID);
        intent.putExtra("authority", true);
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

}

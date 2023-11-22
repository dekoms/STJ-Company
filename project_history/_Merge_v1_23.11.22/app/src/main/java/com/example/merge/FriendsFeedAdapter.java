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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class FriendsFeedAdapter extends RecyclerView.Adapter<FriendsFeedAdapter.FeedViewHolder> {

    DatabaseReference mDatabaseReference;
    StorageReference mStorageReference;

    private List<FeedItem> feedList;
    private Context context;

    FriendsFeedAdapter(List<FeedItem> feedList, Context context){
        this.context = context;
        this.feedList = feedList;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mStorageReference = FirebaseStorage.getInstance().getReference("PetMe");
    }

    @NonNull
    @Override
    public FriendsFeedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FriendsFeedAdapter.FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsFeedAdapter.FeedViewHolder holder, int position) {
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
        intent.putExtra("authority", false);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        public android.widget.TextView TextView;
        public ImageView mediaView;
        public LinearLayout feedLayout;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView = itemView.findViewById(R.id.TextView);
            mediaView = itemView.findViewById(R.id.mediaView);
            feedLayout = itemView.findViewById(R.id.feedLayout);
        }
    }
}

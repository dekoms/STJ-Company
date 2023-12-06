package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.util.*;

public class FeedDetailsActivity extends AppCompatActivity {
    Button back, LikeButton, commentButton, LikeCount, delete;
    EditText comment;
    TextView captionView;
    LinearLayout commentLayout;

    boolean like = false;
    boolean authority;
    int count;

    String tokenID;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);

        Intent intent = getIntent();
        if (intent != null) {
            String registrationDate = intent.getStringExtra("registrationDate");
            String ImageURL = intent.getStringExtra("ImageURL");
            String caption = intent.getStringExtra("caption");
            tokenID = intent.getStringExtra("tokenID");
            authority = intent.getBooleanExtra("authority",false);

            TextView usernameTextView = findViewById(R.id.usernameTextView);
            ImageView mediaView = findViewById(R.id.mediaView);
            captionView = findViewById(R.id.captionTextView);
            LikeButton = findViewById(R.id.likeButton);
            comment = findViewById(R.id.commentEditText);
            commentButton = findViewById(R.id.commentButton);
            commentLayout = findViewById(R.id.commentsLayout);
            LikeCount = findViewById(R.id.likeCount);
            delete = findViewById(R.id.delete);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
            mFirebaseAuth = FirebaseAuth.getInstance();

            isLike();
            setComment();
            setLikeCount();

            if(authority) delete.setVisibility(View.VISIBLE);
            else delete.setVisibility(View.GONE);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ImageURL != null){
                        StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(ImageURL);

                        image.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("ImageDelete", "Image deleted successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ImageDelete", "Image deleted failed");
                            }
                        });
                    }

                    mDatabaseReference.child("FeedComment").child(tokenID).removeValue();
                    mDatabaseReference.child("Like").child(tokenID).removeValue();

                    mDatabaseReference.child("Feed").child(mFirebaseAuth.getCurrentUser().getUid()).child(tokenID).removeValue();

                    int position = FeedAdapter.selectedPosition;
                    Feed.feedList.remove(position);
                    Feed.feedAdapter.notifyDataSetChanged();
                    finish();
                }
            });

            LikeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeedDetailsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialog = inflater.inflate(R.layout.like_list, null);

                    builder.setView(dialog);

                    ListView listView = dialog.findViewById(R.id.likeList);
                    ArrayList<String> likeList = new ArrayList<>();

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FeedDetailsActivity.this, R.layout.like_list_item, R.id.userName, likeList);
                    listView.setAdapter(adapter);

                    mDatabaseReference.child("Like").child(tokenID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String target;
                            for(DataSnapshot data : snapshot.getChildren()){
                                target = data.getKey();
                                mDatabaseReference.child("UserAccount").child(target).child("name").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String name = snapshot.getValue(String.class);
                                            likeList.add(name);

                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    AlertDialog alertDialog =  builder.create();
                    alertDialog.show();
                }
            });

            // 사용자 이름과 이미지를 설정하여 상세 내용을 표시
            usernameTextView.setText(registrationDate);

            if(ImageURL == null) mediaView.setImageResource(R.drawable.null_image);
            else{
                StorageReference image = FirebaseStorage.getInstance().getReferenceFromUrl(ImageURL);

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();

                        Glide.with(FeedDetailsActivity.this)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .fitCenter()
                                .into(mediaView);
                    }
                });
            }

            captionView.setText(caption);

            back = findViewById(R.id.Back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            LikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = mFirebaseAuth.getCurrentUser().getUid();

                    if(like){
                        mDatabaseReference.child("Like").child(tokenID).child(ID).removeValue();
                        LikeButton.setBackgroundResource(R.drawable.empty_heart);
                        count--;
                        LikeCount.setText(Integer.toString(count));
                        like = false;
                    }
                    else{
                        mDatabaseReference.child("Like").child(tokenID).child(ID).setValue(true);
                        LikeButton.setBackgroundResource(R.drawable.heart);
                        count++;
                        LikeCount.setText(Integer.toString(count));
                        like = true;
                    }
                }
            });

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = mFirebaseAuth.getCurrentUser().getUid();
                    mDatabaseReference.child("UserAccount").child(ID).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.getValue(String.class);

                            String comments = comment.getText().toString();

                            if (!comments.isEmpty()) {
                                TextView commentTextView = new TextView(FeedDetailsActivity.this);
                                commentTextView.setText(name + " : " + comments);
                                commentTextView.setTextSize(16);

                                commentLayout.addView(commentTextView);

                                comment.setText("");
                            }

                            mDatabaseReference.child("FeedComment").child(tokenID).child(name).setValue(comments);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FeedDetailsActivity", String.valueOf(error.toException()));
                        }
                    });
                }
            });
        }
    }

    void setComment(){
        mDatabaseReference.child("FeedComment").child(tokenID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name;
                String comments;

                for(DataSnapshot data : snapshot.getChildren()){
                    name = data.getKey();
                    comments = data.getValue(String.class);

                    TextView commentTextView = new TextView(FeedDetailsActivity.this);
                    commentTextView.setText(name + " : " + comments);
                    commentTextView.setTextSize(16);

                    commentLayout.addView(commentTextView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FeedDetailsActivity", String.valueOf(error.toException()));
            }
        });
    }

    void isLike(){
        String id = mFirebaseAuth.getCurrentUser().getUid();

        mDatabaseReference.child("Like").child(tokenID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if(id.equals(data.getKey())){
                        like = true;
                        LikeButton.setBackgroundResource(R.drawable.heart);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FeedDetailsActivity", String.valueOf(error.toException()));
            }
        });
    }

    void setLikeCount(){
        mDatabaseReference.child("Like").child(tokenID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = (int)snapshot.getChildrenCount();
                LikeCount.setText(Integer.toString(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
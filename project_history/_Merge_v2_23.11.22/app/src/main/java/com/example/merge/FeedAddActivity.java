package com.example.merge;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import java.util.*;

public class FeedAddActivity extends AppCompatActivity {

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mFirebaseAuth;

    String name;

    private Button add, back;
    private EditText comment;
    private TextView isSelected;

    private FeedItem newItem;
    private ImageView addimageview, image;
    private Uri selectedImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_add);

        FirebaseApp.initializeApp(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        mStorageReference = FirebaseStorage.getInstance().getReference("PetMe");

        name = mFirebaseAuth.getCurrentUser().getUid();

        add = findViewById(R.id.Add);
        back = findViewById(R.id.Back);
        comment = findViewById(R.id.Comment);
        addimageview = findViewById(R.id.AddImageView);
        isSelected = findViewById(R.id.isSelected);
        image = findViewById(R.id.AddImageView);

        isSelected.setText("No Image!");

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newItem = new FeedItem();

                String randomID = UUID.randomUUID().toString();

                String Comment = comment.getText().toString();
                newItem.setCaption(Comment);
                newItem.setDate();
                newItem.setTokenID(randomID);

                if(selectedImage != null){

                    String imageName = newItem.getDate();
                    StorageReference ImageReference = mStorageReference.child("images/" + name).child(imageName).child(randomID);

                    UploadTask uploadTask = ImageReference.putFile(selectedImage);

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }

                            return ImageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri = task.getResult();

                                newItem.setImageURL(downloadUri.toString());

                                mDatabaseReference.child("Feed").child(mFirebaseAuth.getCurrentUser().getUid()).child(randomID).setValue(newItem);

                                Feed.feedList.add(0, newItem);
                                Feed.feedAdapter.notifyDataSetChanged();

                                Toast.makeText(FeedAddActivity.this, "업로드 완료 !", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(FeedAddActivity.this, "업로드 실패 !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    newItem.setImageURL(null);

                    mDatabaseReference.child("Feed").child(mFirebaseAuth.getCurrentUser().getUid()).child(randomID).setValue(newItem);

                    Feed.feedList.add(0, newItem);
                    Feed.feedAdapter.notifyDataSetChanged();
                }

                finish();
            }
        });

        addimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            selectedImage = data.getData();
            image.setImageURI(selectedImage);
            isSelected.setText("Selected !");
        }
    }
}
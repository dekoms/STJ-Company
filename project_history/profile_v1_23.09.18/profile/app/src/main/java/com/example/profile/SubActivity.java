package com.example.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SubActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;

    Uri uri;

    private Button btn_move2;
    private EditText ed_name;
    private String str;
    private ImageView imageView;

    private DatabaseReference myRef;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        imageView = findViewById(R.id.imageButton2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);

                ActivityResultLauncher.launch(intent);

            }
        });

        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("User");

        ed_name = findViewById(R.id.ed_name);

        Intent intent = getIntent();
        String str2 = intent.getStringExtra("str2");
        ed_name.setText(str2);





        btn_move2 = findViewById(R.id.btn_move2);
        btn_move2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = ed_name.getText().toString();


                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                intent.putExtra("str", str);
                startActivity(intent);
            }
        });

    }

    ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){

                public void onActivityResult(ActivityResult result){
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){

                        uri = result.getData().getData();


                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

        }
    });


}
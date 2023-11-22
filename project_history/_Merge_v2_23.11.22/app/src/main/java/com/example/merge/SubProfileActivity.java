package com.example.merge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SubProfileActivity extends AppCompatActivity {

    private Intent intent;

    String tvImage;
    String tvname;
    String tvage;
    String tvbirth;
    String tvgender;
    private ImageView image;
    private TextView name;
    private TextView age;
    private TextView birth;
    private TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_profile);

        name = findViewById(R.id.tv_animal_name);
        age = findViewById(R.id.tv_animal_age);
        birth = findViewById(R.id.tv_animal_birth);
        gender = findViewById(R.id.tv_animal_gender);
        image = findViewById(R.id.tv_animal_image);
        intent = getIntent();
        tvImage = intent.getStringExtra("tvimage");
        tvname = intent.getStringExtra("tvname");
        tvage =  intent.getStringExtra("tvage");
        tvbirth = intent.getStringExtra("tvbirth");
        tvgender = intent.getStringExtra("tvgender");


        name.setText(tvname);
        age.setText(tvage);
        birth.setText(tvbirth);
        gender.setText(tvgender);
        Glide.with(image).load(tvImage).into(image);
    }
}
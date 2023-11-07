package com.example.petme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FeedDetailsActivity extends AppCompatActivity {

    Button back;
    TextView captionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);

        Intent intent = getIntent();
        if (intent != null) {
            String registrationDate = intent.getStringExtra("registrationDate");
            int mediaResourceId = intent.getIntExtra("mediaResourceId", 0);
            String caption = intent.getStringExtra("caption");

            TextView usernameTextView = findViewById(R.id.usernameTextView);
            ImageView mediaView = findViewById(R.id.mediaView);
            captionView = findViewById(R.id.captionTextView);

            // 사용자 이름과 이미지를 설정하여 상세 내용을 표시
            usernameTextView.setText(registrationDate);
            mediaView.setImageResource(mediaResourceId);

            captionView.setText(caption);

            back = findViewById(R.id.Back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
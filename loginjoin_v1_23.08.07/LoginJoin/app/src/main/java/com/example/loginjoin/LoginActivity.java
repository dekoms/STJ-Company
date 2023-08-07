package com.example.loginjoin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button b = findViewById(R.id.joinButton);
        b.setOnClickListener(view -> {
            Intent intent = new Intent(this, JoinActivity.class);
            startActivity(intent);
        });

    }
}

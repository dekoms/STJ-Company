package com.example.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Button btn_move;
    private TextView name;
    private TextView Userid;


    private FirebaseAuth mFirebaseAuth;

    private FirebaseDatabase mFirebasedb;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;



    private String Uid;

    private GridView gridView;
    private ArrayAdapter<String> adapter;

    private String str2;







    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);

        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        name.setText(str);



        gridView = findViewById(R.id.gridView);

        // 그리드뷰에 표시할 아이템 배열
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9"};

        // 어댑터 설정
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        gridView.setAdapter(adapter);
















        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebasedb = FirebaseDatabase.getInstance();
        databaseReference = mFirebasedb.getReference("User");

        final FirebaseUser user = mFirebaseAuth.getCurrentUser();


        Userid = findViewById(R.id.Userid);
        Userid.setText(user.getEmail());
























        btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str2 = name.getText().toString();


                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("str2",str2);
                startActivity(intent);
            }
        });




        };
    private void readUser(String uid){

    }


    }







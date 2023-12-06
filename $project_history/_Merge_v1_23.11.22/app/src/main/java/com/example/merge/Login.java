package com.example.merge;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.auth.*;
import com.google.firebase.storage.*;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.*;

public class Login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                         // firebase 인증
    private DatabaseReference mDatabaseReference;               // realtime database
    private EditText input_email, input_pwd;                    // email, pwd 버튼

    Button register, login;
    CheckBox autoLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Email = "", PWD = "";
    static boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");
        sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
        input_email = findViewById(R.id.id_input);
        input_pwd = findViewById(R.id.pwd_input);

        autoLogin = findViewById(R.id.autoLogin);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isChecked = true;
                } else {
                    isChecked = false;
                    editor.clear();
                    editor.apply();
                }
            }
        }); // 자동 로그인 체크했을 때의 처리
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 요청

                Email = input_email.getText().toString();
                PWD = input_pwd.getText().toString();        // 6자리 이상

                mFirebaseAuth.signInWithEmailAndPassword(Email, PWD).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Login 성공
                            if(isChecked){
                                sharedPreferences= getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                                editor = sharedPreferences.edit();
                                editor.putString("id",  Email);
                                editor.putString("password", PWD);
                                editor.apply();
                            }
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        }
                        else{
                            Toast.makeText(Login.this, "로그인 실패 !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }); // 로그인 버튼 눌렀을 때의 처리
        if(sharedPreferences != null){
            Email = sharedPreferences.getString("id","");
            PWD = sharedPreferences.getString("password", "");
            try {
                mFirebaseAuth.signInWithEmailAndPassword(Email, PWD).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // 현재 액티비티 종료
                    }
                });
            }catch (IllegalArgumentException e) {

            }
        } // 자동 로그인 처리하게 하는 코드

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 액티비티 활성화
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        }); // 회원가입 버튼 눌렀을 때 화면 전환 처리

    }
}
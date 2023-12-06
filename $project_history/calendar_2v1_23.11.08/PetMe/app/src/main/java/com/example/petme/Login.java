package com.example.petme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;                         // firebase 인증
    private DatabaseReference mDatabaseReference;               // realtime database
    private EditText input_email, input_pwd;                    // email, pwd 버튼

    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PetMe");

        input_email = findViewById(R.id.id_input);
        input_pwd = findViewById(R.id.pwd_input);


        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 요청

                String Email = input_email.getText().toString();
                String PWD = input_pwd.getText().toString();        // 6자리 이상

                mFirebaseAuth.signInWithEmailAndPassword(Email, PWD).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Login 성공
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
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 액티비티 활성화
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
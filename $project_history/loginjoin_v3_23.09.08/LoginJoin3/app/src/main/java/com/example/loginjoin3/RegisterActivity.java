package com.example.loginjoin3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef, mDatabaseRef1; //실시간 데이터베이스
    private EditText emailID, name, password, pwdcheck;
    private Button accept, duplicateButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PetMe");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("PetMe").child("UserAccount");
        emailID = findViewById(R.id.editId1);
        name = findViewById(R.id.editName);
        password = findViewById(R.id.editPwd);
        pwdcheck = findViewById(R.id.editRepwd);
        duplicateButton = findViewById(R.id.dupButton);
        duplicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = emailID.getText().toString();
                String strName = name.getText().toString();
                mDatabaseRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String key = dataSnapshot.child("name").getValue(String.class);
                            if(key.equals(strName)){
                                Toast.makeText(RegisterActivity.this, "중복된 닉네임입니다", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(RegisterActivity.this, "사용 가능한 닉네임입니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        accept = findViewById(R.id.acceptJoin);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strName = name.getText().toString();
                String strEmail = emailID.getText().toString();
                String strPwd = password.getText().toString();
                String strPwdCheck = pwdcheck.getText().toString();
                //firebase auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!strPwd.equals(strPwdCheck)){
                            Toast.makeText(RegisterActivity.this, "비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount userAccount = new UserAccount();
                            userAccount.setName(strName);
                            userAccount.setIdToken(firebaseUser.getUid());
                            userAccount.setEmailID(firebaseUser.getEmail());
                            userAccount.setPassword(strPwd);

                            //setValue : 데이터베이스에 insert 하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount); //여기 고쳐야 됨
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}

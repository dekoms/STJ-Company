package com.example.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.GridView;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private TextView name;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebasedb;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> adapter;
    private ImageView imageView;
    Uri uri;
    private Uri selectedImageUri;
    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프로필 사진 클릭 시 갤러리 열기(프로필 사진 변경)
        imageView = findViewById(R.id.account_iv_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);

                ActivityResultLauncher.launch(intent);

            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebasedb = FirebaseDatabase.getInstance();
        databaseReference = mFirebasedb.getReference("User");
        //현재 사용자 정보 가져오기
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        //이메일 주소를 텍스트뷰에 표시
        name = findViewById(R.id.name);
        name.setText(user.getEmail());

        // 리사이클러뷰 설정
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 데이터 어댑터 및 배열리스트 초기화
        arrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);
        // 등록 버튼 클릭 시 갤러리 열기(동물사진)
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    // 갤러리 열기(동물사진)
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    // 갤러리에서 이미지 선택 후 결과 처리(동물사진)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            showAddDataDialog();
        }
    }

    // 데이터 입력 다이얼로그 표시
    private void showAddDataDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_data_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        final EditText editTextBirth = dialogView.findViewById(R.id.editTextBirth);
        final RadioGroup radioGroupGender = dialogView.findViewById(R.id.radioGroupGender);

        dialogBuilder.setTitle("애완동물 등록");
        dialogBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String birth = editTextBirth.getText().toString();

                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                RadioButton selectedGenderRadioButton = dialogView.findViewById(selectedGenderId);
                String gender = selectedGenderRadioButton.getText().toString();

                MainData mainData = new MainData(selectedImageUri, name, age, gender, birth);
                arrayList.add(mainData);
                mainAdapter.notifyDataSetChanged(); // 새로고침
            }
        });

        dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // 취소 버튼을 누를 때 아무 동작 없음
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



    // ActivityResultLauncher를 사용하여 갤러리에서 이미지 선택 후 이미지뷰에 표시(프로필 사진 변경)
    androidx.activity.result.ActivityResultLauncher<Intent> ActivityResultLauncher = registerForActivityResult(
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







package com.example.softwarecalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView title;
    CalendarView cal;
    TextView date;
    ListView listView;
    EditText et_todo;
    Button btn_add;

    TodoAdapter adapter;
    String[] times = {"8시", "8시", "8시"};
    String time = "8시";
    String[] contents = {"산책 시키기", "산책 시키기", "산책 시키기"};
    static ArrayList<Todo> list;

    ActivityResultLauncher<Intent> activityResultLauncher;

    TodoApi todoApi;
    String BaseUrl = "http://stjcompany.dothome.co.kr/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == 0){
                    Log.i("디버깅", "디버깅 확인");
                    Toast.makeText(MainActivity.this, "반환 성공", Toast.LENGTH_SHORT).show();
                    Intent data = result.getData();
                    if(data!=null){
                        ArrayList<Todo> updatedList = (ArrayList<Todo>) data.getSerializableExtra("updatedAdapterList");
                        list = updatedList;
                    }
                } else{
                    Log.i("디버깅", "디버깅 실패");
                    Toast.makeText(MainActivity.this, "반환 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });




        cal = findViewById(R.id.cal);
        date = findViewById(R.id.date);

        long currentTime = System.currentTimeMillis();
        Date today = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String getTime = sdf.format(today);

        date.setText(getTime);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String Month = String.valueOf(month);
                String Day = String.valueOf(day);

                Month = (Month.length() < 2) ? ("0" + month) : (Month);
                Day = (Day.length() < 2) ? ("0" + day) : (Day);

                date.setText(year+"년 "+Month+"월 "+Day+"일");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this, SubActivity.class);
                mainIntent.putExtra("year", date.getText().toString().substring(0,4));
                mainIntent.putExtra("month", date.getText().toString().substring(6,8));
                mainIntent.putExtra("day", date.getText().toString().substring(10,12));
                mainIntent.putExtra("adapterList", list);
                activityResultLauncher.launch(mainIntent);
            }
        });




        listView = findViewById(R.id.listView);
        // --- 이 부분은 맨 처음 한번만 실행 되도록 수정 필요 ---
        //ArrayList<Todo>
        list = new ArrayList<>();
        //아이템 하나씩 추가
        for(int i =0 ;i<times.length;i++){
            Todo todo = new Todo(list.size(), "", times[i], contents[i], "2023-08-22");
            list.add(todo);
        }
        adapter = new TodoAdapter(this, list, R.layout.listview_layout);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, contents[i], Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }
        });


        et_todo = findViewById(R.id.et_todo);
        btn_add = findViewById(R.id.btn_add);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = new Todo(list.size(), "", "8시", et_todo.getText().toString(), "2023-08-22");
                list.add(todo);
                adapter.notifyDataSetChanged();
                et_todo.setText("");


                insertTodo();
            }

            private void insertTodo() {
                //contents에 값을 넣는 걸로 가야할 듯? 아니면 list[i].todo.key
                String content = et_todo.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                todoApi = retrofit.create(TodoApi.class);
                Call<TodoModel> todoModelCall = todoApi.insertTodo("", "8시", contents[0], "2023-08-22");
                todoModelCall.enqueue(new Callback<TodoModel>() {
                    @Override
                    public void onResponse(Call<TodoModel> call, Response<TodoModel> response) {
                        Toast.makeText(MainActivity.this, "데이터 삽입 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<TodoModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "데이터 삽입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}
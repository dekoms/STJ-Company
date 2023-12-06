package com.example.petme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends Fragment{

    public Home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        TextView time = rootView.findViewById(R.id.textView);
        time.setText(getTime());

        CheckBox checkBox1 = rootView.findViewById(R.id.checkBox1);
        checkBox1.setText("산책 시키기");
        CheckBox checkBox2 = rootView.findViewById(R.id.checkBox2);
        checkBox2.setText("병원 다녀오기");

        return rootView;
    }

    public String getTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}

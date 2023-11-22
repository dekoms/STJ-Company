package com.example.petme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Feed extends Fragment{
    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;

    Button management, add;

    public Feed() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed, container, false);

        /*
        TextView textView = rootView.findViewById(R.id.textView2);
        textView.setText("여기는 두번째 fragment");
        */

        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedAdapter = new FeedAdapter(getSampleData(), getActivity()); // 샘플 데이터를 얻어와서 Adapter에 전달
        recyclerView.setAdapter(feedAdapter);

        return rootView;
    }

    private List<FeedItem> getSampleData() {
        List<FeedItem> feedList = new ArrayList<>();
        feedList.add(new FeedItem(R.drawable.mali_sample1, "말리 산책"));
        feedList.add(new FeedItem(R.drawable.mali_sample2, "애기 말리"));
        feedList.add(new FeedItem(R.drawable.mali_sample3, "벚꽃 말리"));
        feedList.add(new FeedItem(R.drawable.mali_sample4, "빼꼼 말리"));
        feedList.add(new FeedItem(R.drawable.mali_sample5, "말리 증명사진 ( 근데 강아지한테 증명사진이 왜 필요함? )"));
        feedList.add(new FeedItem(R.drawable.sample_image1, "히히 오줌 발싸"));
        feedList.add(new FeedItem(R.drawable.sample_image2, "히히 오줌 발싸2"));
        feedList.add(new FeedItem(R.drawable.sample_image3, "히히 오줌 발싸3"));
        feedList.add(new FeedItem(R.drawable.sample_image4, "히히 오줌 발싸4"));
        feedList.add(new FeedItem(R.drawable.sample_image5, "히히 오줌 발싸5"));
        return feedList;
    }
}

package com.example.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity {

    ListView videoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vilde_list);

        videoListView = findViewById(R.id.video_list);

        final ArrayList<String> videoList = new ArrayList<>();

        videoList.add("Meditaion by Dr.Gk");
        videoList.add("Spiritual Tablet Production");
        videoList.add("Dr Gopala Krishna");
        videoList.add("Dr GK Spiritual Tablets");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(VideoListActivity.this, android.R.layout.simple_list_item_1, videoList);
        videoListView.setAdapter(adapter);


        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoListActivity.this, VideoPlayerActivity.class);
                intent.putExtra("name", videoList.get(position));
                startActivity(intent);
            }
        });
    }
}

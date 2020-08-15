package com.example.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spiritualtablets.adapters.FontListAdapter;

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
        videoList.add("Saxaphone by Sri Srinivas Raju");
        videoList.add("Bunny Master Dance");
        videoList.add("Naadhaswaram");
        videoList.add("Day 1 Athma Soundaryam");
        videoList.add("Day 1 Dhyana Matrutvam");
        videoList.add("Day 1 Evening Cultures 1");
        videoList.add("Day 1 Evening Cultures 2");
        videoList.add("Day 1 Evening Session with Patriji");
        videoList.add("Day 2 Morning Meditation with Brahmarshi Patriji");
        videoList.add("Day 2 Spiritual Tablets Workshop");
        videoList.add("Day 2 Seth Workshop");
        videoList.add("Day 2 Angel Swathi Workshop");
        videoList.add("Day 2 Dhyana Navaratnalu");
        videoList.add("Day 2 Jabardasth Skit");
        videoList.add("Day 2 Matrimony");
        videoList.add("Sangeka Samasyalu by Dr.GK");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Ramani Garu and Group");
        videoList.add("6th Thyagaraja Swami Dhana Aradhanostavalu Prasanthi and Bhavana Kansart");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Raju Gari Kacheri");
        videoList.add("International Yoga Day 2020 Celebrations");
        videoList.add("Night Meditation bt DR.Gk || International Yoga Day 2020 Celebrations");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Pantulu Sandeep Brundam");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Sree Pratha Krishnamurthi Garu and Team");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Pacharatna Seva");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Sri Vidya Garu and Team");
        videoList.add("12hrs Intense Meditation on Guru Pournami Brahmarshi Patriji Maitreyas International");
        videoList.add("Thyagaraja Swami Dhana Aradhanostavalu Veena Meditaion Music by Ramavarapu Madhuri Devi");

        FontListAdapter adapter = new FontListAdapter(VideoListActivity.this, videoList);
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


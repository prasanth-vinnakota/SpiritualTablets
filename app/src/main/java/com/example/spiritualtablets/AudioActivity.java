package com.example.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;

public class AudioActivity extends AppCompatActivity {

    List<DataItem> lstDataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lstDataItem = new ArrayList<>();


        lstDataItem.add(new DataItem("Dhyanam", "Audios", R.drawable.dhyanam));
        lstDataItem.add(new DataItem("Nissahayata", "Audios", R.drawable.nissahayatha));
        lstDataItem.add(new DataItem("Arishadvargalu", "Audios", R.drawable.arishadvargaalu));
        lstDataItem.add(new DataItem("Aadhyathmika Putrulu", "Audios", R.drawable.adyatmika_putrulu));
        lstDataItem.add(new DataItem("Aacharya Saangatyam", "Audios", R.drawable.acharya_sangatyam));
        lstDataItem.add(new DataItem("Nalugu Setruvulu", "Audios", R.drawable.nalugu_setruvulu));
        lstDataItem.add(new DataItem("Manchi Kashtalu", "", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Jeevitha Dhyeyam", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 1", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 2", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 3", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 4", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("G K Sir Speech", "Audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Aathma Kutumba Dharmam", "Audios", R.drawable.audio_cover_page));


        RecyclerView myrv = findViewById(R.id.audio_recycler_view_id);
        AudioRecyclerViewAdapter myAdapter = new AudioRecyclerViewAdapter(getApplicationContext(),lstDataItem);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        myrv.setAdapter(myAdapter);



    }
}

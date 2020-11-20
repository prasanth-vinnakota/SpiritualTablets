package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import developer.prasanth.spiritualtablets.adapters.AudioRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

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


        lstDataItem.add(new DataItem("Dhyanam", "audios", R.drawable.dhyanam));
        lstDataItem.add(new DataItem("Nissahayata", "audios", R.drawable.nissahayatha));
        lstDataItem.add(new DataItem("Arishadvargalu", "audios", R.drawable.arishadvargaalu));
        lstDataItem.add(new DataItem("Aadhyathmika Putrulu", "audios", R.drawable.adyatmika_putrulu));
        lstDataItem.add(new DataItem("Aacharya Saangatyam", "audios", R.drawable.acharya_sangatyam));
        lstDataItem.add(new DataItem("Naluguru Setruvulu", "audios", R.drawable.nalugu_setruvulu));
        lstDataItem.add(new DataItem("Manchi Kashtalu", "", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Jeevitha Dhyeyam", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 1", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 2", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 3", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam 4", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("G K Sir Speech", "audios", R.drawable.audio_cover_page));
        lstDataItem.add(new DataItem("Aathma Kutumba Dharmam", "audios", R.drawable.audio_cover_page));


        RecyclerView myrv = findViewById(R.id.audio_recycler_view_id);
        AudioRecyclerViewAdapter myAdapter = new AudioRecyclerViewAdapter(AudioActivity.this,lstDataItem);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        myrv.setAdapter(myAdapter);



    }
}

package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.AudioRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AudioActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dataItems = new ArrayList<>();


        dataItems.add(new DataItem("Dhyanam", "audios", R.drawable.dhyanam));
        dataItems.add(new DataItem("Nissahayata", "audios", R.drawable.nissahayatha));
        dataItems.add(new DataItem("Arishadvargalu", "audios", R.drawable.arishadvargaalu));
        dataItems.add(new DataItem("Aadhyathmika Putrulu", "audios", R.drawable.adyatmika_putrulu));
        dataItems.add(new DataItem("Aacharya Saangatyam", "audios", R.drawable.acharya_sangatyam));
        dataItems.add(new DataItem("Naluguru Setruvulu", "audios", R.drawable.nalugu_setruvulu));
        dataItems.add(new DataItem("Manchi Kashtalu", "", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Jeevitha Dhyeyam", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Garbaasayam 1", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Garbaasayam 2", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Garbaasayam 3", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Garbaasayam 4", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("G K Sir Speech", "audios", R.drawable.audio_cover_page));
        dataItems.add(new DataItem("Aathma Kutumba Dharmam", "audios", R.drawable.audio_cover_page));


        RecyclerView recyclerView = findViewById(R.id.audio_recycler_view_id);
        AudioRecyclerViewAdapter myAdapter = new AudioRecyclerViewAdapter(AudioActivity.this,dataItems);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(myAdapter);
    }

}

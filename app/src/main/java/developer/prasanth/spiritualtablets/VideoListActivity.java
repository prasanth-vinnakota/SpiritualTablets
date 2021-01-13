package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.YoutubeAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VideoListActivity extends AppCompatActivity{

    DatabaseReference databaseReference;
    List<String> stringList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VideoListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VideoListActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        init();

        final String language = Objects.requireNonNull(getIntent().getStringExtra("language"));

        databaseReference = FirebaseDatabase.getInstance().getReference("youtube").child(language);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        stringList.add(dataSnapshot.getKey());
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
                    YoutubeAdapter youtubeAdapter = new YoutubeAdapter(language, stringList, VideoListActivity.this);
                    recyclerView.setAdapter(youtubeAdapter);
                }
                else {
                    showMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void init(){

        recyclerView = findViewById(R.id.youtube_videos_list);
        stringList = new ArrayList<>();
    }

    private void showMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoListActivity.this);
        builder.setMessage("No Videos Available");
        builder.setCancelable(true);
        builder.create().show();
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}


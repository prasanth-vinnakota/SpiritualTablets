package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.YoutubeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VideoListActivity extends AppCompatActivity{

    DatabaseReference youtube_ref;
    ArrayList<String> youtube_videos_list;
    RecyclerView youtubeRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        youtubeRV = findViewById(R.id.youtube_videos_list);

        youtube_videos_list = new ArrayList<>();

        final String language = Objects.requireNonNull(getIntent().getStringExtra("language"));

        youtube_ref = FirebaseDatabase.getInstance().getReference("youtube").child(language);

        youtube_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        youtube_videos_list.add(dataSnapshot.getKey());
                    }
                    youtubeRV.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
                    YoutubeAdapter youtubeAdapter = new YoutubeAdapter(language, youtube_videos_list, VideoListActivity.this);
                    youtubeRV.setAdapter(youtubeAdapter);
                }
                else {
                    Toast.makeText(VideoListActivity.this, "No Videos Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


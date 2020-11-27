package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.YoutubeAdapter;

import java.util.ArrayList;
import java.util.List;
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
                    showMessage("No Videos Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showMessage(error.getMessage());
            }
        });
    }

    private void init(){

        recyclerView = findViewById(R.id.youtube_videos_list);
        stringList = new ArrayList<>();
    }

    private void showMessage(String message){

        Toast.makeText(VideoListActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}


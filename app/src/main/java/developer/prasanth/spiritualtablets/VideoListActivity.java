package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class VideoListActivity extends AppCompatActivity implements AddVideoDialog.AddVideoListener {

    DatabaseReference youtube_ref, user_admin_ref;
    ArrayList<String> youtube_videos_list;
    RecyclerView youtubeRV;
    FloatingActionButton addVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        youtubeRV = findViewById(R.id.youtube_videos_list);

        youtube_videos_list = new ArrayList<>();

        addVideoButton = findViewById(R.id.add_video_button);

        user_admin_ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("admin");

        user_admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    addVideoButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        youtube_ref = FirebaseDatabase.getInstance().getReference("youtube");

        youtube_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        youtube_videos_list.add(dataSnapshot.getKey());
                    }
                    youtubeRV.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
                    YoutubeAdapter youtubeAdapter = new YoutubeAdapter(youtube_videos_list, VideoListActivity.this);
                    youtubeRV.setAdapter(youtubeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addVideo(View view) {

        AddVideoDialog addVideoDialog = new AddVideoDialog();
        addVideoDialog.show(getSupportFragmentManager(),"Add Video");
    }

    @Override
    public void applyVideoTexts(String video_name, String video_link) {
        if (!TextUtils.isEmpty(video_name) && !TextUtils.isEmpty(video_link)) {

            HashMap<String,Object> map = new HashMap<>();
            map.put(video_name, video_link);

            youtube_ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(VideoListActivity.this, "Video Added Successfully", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VideoListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(VideoListActivity.this, "Video name and Video link must not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}


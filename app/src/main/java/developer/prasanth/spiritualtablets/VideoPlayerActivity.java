package developer.prasanth.spiritualtablets;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import developer.prasanth.spiritualtablets.youtube.YoutubeConfig;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    YouTubePlayerView playerView;
    DatabaseReference databaseReference;
    YouTubePlayer.OnInitializedListener initializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        init();

        playerView.initialize(YoutubeConfig.getApiKey(), initializedListener);
    }

    private void init(){

        playerView = findViewById(R.id.youtubePlay);

        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                String videoName = getIntent().getStringExtra("name");
                String language = getIntent().getStringExtra("language");

                assert videoName != null;
                assert language != null;

                databaseReference = FirebaseDatabase.getInstance().getReference("youtube").child(language).child(videoName);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            youTubePlayer.loadVideo(Objects.requireNonNull(snapshot.getValue()).toString    ());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        showMessage(error.getMessage());
                    }
                });

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                showMessage("Initialization Failure");
            }
        };
    }

    private void showMessage(String  message){

        Toast.makeText(VideoPlayerActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

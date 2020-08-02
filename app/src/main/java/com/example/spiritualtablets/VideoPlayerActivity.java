package com.example.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.spiritualtablets.youtube.YoutubeConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    YouTubePlayerView playerView;
    YouTubePlayer.OnInitializedListener initializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.youtubePlay);

                initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                String videoName = getIntent().getStringExtra("name");
                switch (videoName){

                    case "Meditaion by Dr.Gk":
                        youTubePlayer.loadVideo("awOX1o0ZRaE");
                        break;
                    case "Spiritual Tablet Production":
                        youTubePlayer.loadVideo("J2orPMhn0P4");
                        break;
                    case "Dr Gopala Krishna":
                        youTubePlayer.loadVideo("cyr5-XBe4WA");
                        break;
                    case "Dr GK Spiritual Tablets":
                        youTubePlayer.loadVideo("yA3jsGy4QeA");
                        break;
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        playerView.initialize(YoutubeConfig.getApiKey(), initializedListener);
    }
}

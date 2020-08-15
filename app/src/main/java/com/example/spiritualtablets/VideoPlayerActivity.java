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
                    case "Saxaphone by Sri Srinivas Raju":
                        youTubePlayer.loadVideo("MzIFoBkko4I");
                        break;
                    case "Bunny Master Dance":
                        youTubePlayer.loadVideo("5zZfX76kXQ");
                        break;
                    case "Naadhaswaram":
                        youTubePlayer.loadVideo("CQepJWFiZVY");
                        break;
                    case "Day 1 Athma Soundaryam":
                        youTubePlayer.loadVideo("9nvnstFqaUg");
                        break;
                    case "Day 1 Dhyana Matrutvam":
                        youTubePlayer.loadVideo("sy-Izxgj0KM");
                        break;
                    case "Day 1 Evening Cultures 1":
                        youTubePlayer.loadVideo("O_lWlH7V7UM");
                        break;
                    case "Day 1 Evening Session with Patriji":
                        youTubePlayer.loadVideo("8NDXDxD6SLk");
                        break;
                    case "Day 1 Evening Cultures 2":
                        youTubePlayer.loadVideo("l8YVtLbCXE8");
                        break;
                    case "Day 2 Morning Meditation with Brahmarshi Patriji":
                        youTubePlayer.loadVideo("XtrtJ9lCFG8");
                        break;
                    case "Day 2 Spiritual Tablets Workshop":
                        youTubePlayer.loadVideo("a5tmZpB_JOM");
                        break;
                    case "Day 2 Seth Workshop":
                        youTubePlayer.loadVideo("shfygFdWnME");
                        break;
                    case "Day 2 Angel Swathi Workshop":
                        youTubePlayer.loadVideo("aBsibhACW3c");
                        break;
                    case "Day 2 Dhyana Navaratnalu":
                        youTubePlayer.loadVideo("wd2Rx30Ey4g");
                        break;
                    case "Day 2 Jabardasth Skit":
                        youTubePlayer.loadVideo("hZU4TRi9ws8");
                        break;
                    case "Day 2 Matrimony":
                        youTubePlayer.loadVideo("cfgpW66T-WE");
                        break;
                    case "Sangeka Samasyalu by Dr.GK":
                        youTubePlayer.loadVideo("Y8CpxCiNtQ4");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Ramani Garu and Group":
                        youTubePlayer.loadVideo("fgnqLux7wJY");
                        break;
                    case "6th Thyagaraja Swami Dhana Aradhanostavalu Prasanthi and Bhavana Kansart":
                        youTubePlayer.loadVideo("cLFBLbLOzCw");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Raju Gari Kacheri":
                        youTubePlayer.loadVideo("YTtPtFnWS-Q");
                        break;
                    case "International Yoga Day 2020 Celebrations":
                        youTubePlayer.loadVideo("14qnt8uOCh8");
                        break;
                    case "Night Meditation bt DR.Gk || International Yoga Day 2020 Celebrations":
                        youTubePlayer.loadVideo("tFFQLtdHnkI");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Pantulu Sandeep Brundam":
                        youTubePlayer.loadVideo("BotyY6q12uI");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Sree Pratha Krishnamurthi Garu and Team":
                        youTubePlayer.loadVideo("vShCa6ivZX8");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Pacharatna Seva":
                        youTubePlayer.loadVideo("c9VWeY1vKco");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Sri Vidya Garu and Team":
                        youTubePlayer.loadVideo("TGdKD5qIBo0");
                        break;
                    case "12hrs Intense Meditation on Guru Pournami Brahmarshi Patriji Maitreyas International":
                        youTubePlayer.loadVideo("txfQ6mw5eVQ");
                        break;
                    case "Thyagaraja Swami Dhana Aradhanostavalu Veena Meditaion Music by Ramavarapu Madhuri Devi":
                        youTubePlayer.loadVideo("fTKCPwKEvcc");
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

package developer.prasanth.spiritualtablets;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import developer.prasanth.spiritualtablets.youtube.YoutubeConfig;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    YouTubePlayerView playerView;
    YouTubePlayer.OnInitializedListener initializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        init();

        playerView.initialize(YoutubeConfig.getApiKey(), initializedListener);
    }

    private void init(){

        playerView = findViewById(R.id.youtubePlay);

        initializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                String language = getIntent().getStringExtra("language");
                String link = getIntent().getStringExtra("link");
                String key = getIntent().getStringExtra("key");

                youTubePlayer.loadVideo(link);

                String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("Videos").child(language).child(key);
                user_ref.addValueEventListener(new ValueEventListener() {
                    boolean check = true;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (check){
                            long count = snapshot.getChildrenCount();
                            count++;
                            user_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                        }
                        check = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference video_ref  = FirebaseDatabase.getInstance().getReference("Youtube").child(language).child(key).child("Users").child(current_user_id);
                video_ref.addValueEventListener(new ValueEventListener() {
                    boolean check = true;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (check){
                            long count = snapshot.getChildrenCount();
                            count++;
                            video_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                        }
                        check = false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                showMessage();
            }
        };
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
    private String getDateAndTime() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar).toString();
    }

    private void showMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this);
        builder.setMessage("Initialization Failure");
        builder.setCancelable(true);
        builder.create().show();
    }
}

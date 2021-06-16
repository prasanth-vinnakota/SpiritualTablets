package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView musicName;
    TextView totalDuration;
    TextView currentPosition;
    ImageView playOrPause;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicPlayerActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicPlayerActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        init();

        playOrPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {

                handler.removeCallbacks(updater);
                mediaPlayer.pause();
                playOrPause.setImageResource(R.drawable.play);
            } else {

                mediaPlayer.start();
                playOrPause.setImageResource(R.drawable.pause);
                updateSeekBar();
            }
        });

        try {

            mediaPlayer.setDataSource(getIntent().getStringExtra("link"));
            mediaPlayer.prepare();
            totalDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
        } catch (IOException e) {

            showMessage(e.getMessage());
        }

        seekBar.setOnTouchListener((v, event) -> {

            SeekBar seek_bar = (SeekBar) v;
            int play_position = (mediaPlayer.getDuration() / 100) * seek_bar.getProgress();
            mediaPlayer.seekTo(play_position);
            return false;
        });
    }

    private void init(){

        musicName = findViewById(R.id.activity_music_player_music_name);
        totalDuration = findViewById(R.id.activity_music_player_total_duration);
        currentPosition = findViewById(R.id.activity_music_player_timer);

        playOrPause = findViewById(R.id.activity_music_player_play_pause);

        seekBar = findViewById(R.id.activity_music_player_seek_bar);

        mediaPlayer = new MediaPlayer();
        String audioName = getIntent().getStringExtra("name");
        musicName.setText(audioName);
        String time = getIntent().getStringExtra("time");

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("Music").child(time).child(audioName);
        user_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check) {
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
        DatabaseReference audios_ref = FirebaseDatabase.getInstance().getReference("Music").child(time).child(audioName).child("Users").child(current_user_id);
        audios_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check){
                    long count = snapshot.getChildrenCount();
                    count++;
                    audios_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                }
                check = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private final Runnable updater = new Runnable() {
        @Override
        public void run() {

            updateSeekBar();
            long current = mediaPlayer.getCurrentPosition();
            currentPosition.setText(milliSecondsToTimer(current));
        }
    };

    private void updateSeekBar() {

        if (mediaPlayer.isPlaying()) {

            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
        else
            playOrPause.setImageResource(R.drawable.play);
    }

    private String milliSecondsToTimer(long milliSeconds) {

        String timerString = "";
        String secondString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0)
            timerString = hours + ":";

        if (seconds < 10)
            secondString = "0" + seconds;
        else
            secondString = "" + seconds;


        timerString = timerString + minutes + ":" + secondString;

        return timerString;
    }

    private void showMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        mediaPlayer.stop();
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
}
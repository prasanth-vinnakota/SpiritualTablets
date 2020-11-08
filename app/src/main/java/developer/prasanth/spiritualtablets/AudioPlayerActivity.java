package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.R;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity {


    TextView songName, totalDuration, currentPosition;
    ImageView playOrPause, bookImage;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        songName = findViewById(R.id.song_name);
        totalDuration = findViewById(R.id.total_duration);
        currentPosition = findViewById(R.id.timer);

        playOrPause = findViewById(R.id.play_pause);
        bookImage = findViewById(R.id.audio_image_view);

        seekBar = findViewById(R.id.seek_bar);

        mediaPlayer = new MediaPlayer();
        songName.setText(getIntent().getStringExtra("song_name"));
        bookImage.setImageResource(getIntent().getIntExtra("image",0));

        playOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    playOrPause.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playOrPause.setImageResource(R.drawable.pause);
                    updateSeekBar();
                }
            }
        });

        try {
            mediaPlayer.setDataSource(getIntent().getStringExtra("url"));
            mediaPlayer.prepare();
            totalDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                SeekBar seek_bar = (SeekBar)v;
                int play_position = (mediaPlayer.getDuration() / 100 ) * seek_bar.getProgress();
                mediaPlayer.seekTo(play_position);
                return false;
            }
        });

    }

    private Runnable updater = new Runnable() {
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
    }

    private String milliSecondsToTimer(long milliSeconds) {

        String timerString = "";
        String secondString = "";

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondString;

        return timerString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}

package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MusicMeditationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_meditation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicMeditationActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicMeditationActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void fiftyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","50");
        startActivity(intent);
    }

    public void sixtyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","60");
        startActivity(intent);
    }

    public void thirtyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","30");
        startActivity(intent);
    }

    public void fortyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","40");
        startActivity(intent);
    }

    public void fifteenMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","15");
        startActivity(intent);
    }

    public void twentyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","20");
        startActivity(intent);
    }

    public void fiveMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","05");
        startActivity(intent);
    }

    public void tenMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","10");
        startActivity(intent);
    }

    public void seventyFiveMinutes(View view) {

        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","75");
        startActivity(intent);
    }

    public void ninetyMinutes(View view) {
        Intent intent = new Intent(MusicMeditationActivity.this,MusicListActivity.class);
        intent.putExtra("time","90");
        startActivity(intent);
    }
}
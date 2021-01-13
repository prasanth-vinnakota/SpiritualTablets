package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
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

import developer.prasanth.spiritualtablets.about_us.FounderActivity;
import developer.prasanth.spiritualtablets.about_us.HistoryActivity;
import developer.prasanth.spiritualtablets.about_us.IntroductionActivity;
import developer.prasanth.spiritualtablets.about_us.PrincipleActivity;
import developer.prasanth.spiritualtablets.about_us.PyramidDoctorsActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AboutUsActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AboutUsActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void introduction(View view) {

        startActivity(new Intent(this, IntroductionActivity.class));
    }

    public void history(View view) {

        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void principle(View view) {

        startActivity(new Intent(this, PrincipleActivity.class));
    }

    public void pyramidDoctors(View view) {

        startActivity(new Intent(this, PyramidDoctorsActivity.class));
    }

    public void outlets(View view) {

        startActivity(new Intent(this, OutletsActivity.class));
    }

    public void founder(View view) {

        startActivity(new Intent(this, FounderActivity.class));
    }


    public void facebook(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.facebook.com/spiritualhealth.care"));
        startActivity(intent);
    }

    public void youtubeTelugu(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/channel/UCvdbtwFCC-4OYU7zy_bM9aw"));
        startActivity(intent);
    }

    public void youtubeEnglish(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/channel/UCRIdLxE7-YefPnNJOraYThQ"));
        startActivity(intent);
    }

    public void youtubeHindi(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/channel/UCa7wUGcDCsX0KfNQnX-WNeg/videos"));
        startActivity(intent);
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}

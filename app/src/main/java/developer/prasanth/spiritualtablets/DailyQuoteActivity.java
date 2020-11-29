package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("ALL")
public class DailyQuoteActivity extends AppCompatActivity {

    TextView title;
    TextView description;
    ImageView image;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_quote);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        final Date date = new Date();

        DatabaseReference dailyTipReference = FirebaseDatabase.getInstance().getReference("daily_tip").child(timestampToString(date.getTime()));

        dailyTipReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                            if (dataSnapshot.child("Title").getValue() != null) {

                                title.setText(Objects.requireNonNull(dataSnapshot.child("Title").getValue().toString()));
                                title.setVisibility(View.VISIBLE);
                            }

                            if (dataSnapshot.child("Image").getValue() != null) {

                                Glide.with(DailyQuoteActivity.this).load(dataSnapshot.child("Image").getValue().toString()).into(image);
                                image.setVisibility(View.VISIBLE);
                            }

                            if (dataSnapshot.child("Description").getValue() != null) {

                                description.setText(Objects.requireNonNull(dataSnapshot.child("Description").getValue().toString()));
                                description.setVisibility(View.VISIBLE);
                            }

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                showMessage(databaseError.getMessage());
            }
        });
    }

    private void init(){

        progressBar = findViewById(R.id.daily_tip_progressbar);
        title = findViewById(R.id.daily_tip_title);
        description = findViewById(R.id.daily_tip_description);

        image = findViewById(R.id.daily_tip_image);
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    private void showMessage(String message){
        Toast.makeText(DailyQuoteActivity.this, message, Toast.LENGTH_LONG).show();
    }

}


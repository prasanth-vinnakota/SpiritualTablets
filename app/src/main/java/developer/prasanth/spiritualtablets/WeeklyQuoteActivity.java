package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

@SuppressWarnings("ALL")
public class WeeklyQuoteActivity extends AppCompatActivity {

    TextView title;
    TextView description;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_quote);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(WeeklyQuoteActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(WeeklyQuoteActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        init();

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("Weekly Quotes");
        user_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check){
                    long count = snapshot.getChildrenCount();
                    count++;
                    user_ref.child(String.valueOf(count)).setValue(getDate());
                }
                check = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference weekly_quotes_ref = FirebaseDatabase.getInstance().getReference("Weekly Quote").child("Users").child(current_user_id);

        weekly_quotes_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check){
                    long count = snapshot.getChildrenCount();
                    count++;
                    weekly_quotes_ref.child(String.valueOf(count)).setValue(getDate());
                }
                check = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference dailyTipReference = FirebaseDatabase.getInstance().getReference("Weekly Quote").child(getDate());

        dailyTipReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                            if (dataSnapshot.child("Title").getValue() != null) {

                                title.setText(Objects.requireNonNull(dataSnapshot.child("Title").getValue().toString()));
                                title.setVisibility(View.VISIBLE);
                            }
                            if (dataSnapshot.child("Description").getValue() != null) {

                                description.setText(Objects.requireNonNull(dataSnapshot.child("Description").getValue().toString()));
                                description.setVisibility(View.VISIBLE);
                            }

                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(WeeklyQuoteActivity.this, "Today's Quote Is Not Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void init(){

        progressBar = findViewById(R.id.daily_tip_progressbar);
        title = findViewById(R.id.daily_tip_title);
        description = findViewById(R.id.daily_tip_description);

    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

}


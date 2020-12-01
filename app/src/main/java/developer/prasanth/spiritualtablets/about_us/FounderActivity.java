package developer.prasanth.spiritualtablets.about_us;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.VideoPlayerActivity;

public class FounderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(FounderActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(FounderActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void loadGkSirSpeech(View view) {
        Intent intent = new Intent(FounderActivity.this, VideoPlayerActivity.class);
        intent.putExtra("name", "Dr GK Spiritual Tablets");
        intent.putExtra("language","telugu");
        startActivity(intent);

    }
}

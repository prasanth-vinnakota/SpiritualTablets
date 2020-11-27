package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewEventActivity extends AppCompatActivity {

    TextView eventName;
    TextView eventNameTv;
    TextView eventDescription;
    TextView eventDescriptionTv;
    TextView eventTiming;
    TextView eventTimingTv;
    ImageView eventImage;
    LinearLayout linearLayout;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        init();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (snapshot.child("name").getValue() != null){

                        eventName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                    }
                    else {

                        eventNameTv.setVisibility(View.GONE);
                        eventName.setVisibility(View.GONE);
                    }
                    if (snapshot.child("description").getValue() != null){

                        eventDescription.setText(Objects.requireNonNull(snapshot.child("description").getValue()).toString());
                    }
                    else {

                        eventDescriptionTv.setVisibility(View.GONE);
                        eventDescription.setVisibility(View.GONE);
                    }
                    if (snapshot.child("image").getValue() != null){

                        Glide.with(ViewEventActivity.this).load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(eventImage);
                    }
                    else {

                        eventImage.setVisibility(View.GONE);
                    }
                    if (snapshot.child("timing").getValue() != null){

                        eventTiming.setText(Objects.requireNonNull(snapshot.child("timing").getValue()).toString());
                    }
                    else {

                        eventTiming.setVisibility(View.GONE);
                        eventTimingTv.setVisibility(View.GONE);
                    }

                    if (snapshot.child("link").getValue() != null){

                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(Objects.requireNonNull(snapshot.child("link").getValue()).toString()));
                                startActivity(intent);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showMessage(error.getMessage());
            }
        });


    }

    private void init(){

        eventName = findViewById(R.id.event_name);
        eventNameTv = findViewById(R.id.event_name_tv);
        eventDescription = findViewById(R.id.event_description);
        eventDescriptionTv = findViewById(R.id.event_description_tv);
        eventTiming = findViewById(R.id.event_timing);
        eventTimingTv = findViewById(R.id.event_timing_tv);

        eventImage = findViewById(R.id.events_image);
        linearLayout = findViewById(R.id.event_item_linear_layout_view);

        reference = FirebaseDatabase.getInstance().getReference("events").child(Objects.requireNonNull(getIntent().getStringExtra("language"))).child(Objects.requireNonNull(getIntent().getStringExtra("name")));
    }

    private void showMessage(String message){

        Toast.makeText(ViewEventActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
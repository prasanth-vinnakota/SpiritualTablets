package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.WindowManager;


import developer.prasanth.spiritualtablets.adapters.EventAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsByLanguageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    DatabaseReference reference;
    List<String> list;
    EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_by_language);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(EventsByLanguageActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(EventsByLanguageActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        loadingDialog = new LoadingDialog(EventsByLanguageActivity.this);

        loadingDialog.startLoading();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.events_by_language_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventsByLanguageActivity.this));

        reference = FirebaseDatabase.getInstance().getReference().child("events").child(Objects.requireNonNull(getIntent().getStringExtra("language")));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(dataSnapshot.getKey());
                    }
                    eventAdapter = new EventAdapter(EventsByLanguageActivity.this,list,getIntent().getStringExtra("language"));
                    recyclerView.setAdapter(eventAdapter);
                }
                else
                    showMessage();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EventsByLanguageActivity.this);
        builder.setCancelable(true);
        builder.setMessage("No Events Found");
        builder.create().show();

    }
}

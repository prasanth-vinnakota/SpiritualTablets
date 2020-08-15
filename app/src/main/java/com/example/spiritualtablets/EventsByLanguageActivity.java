package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spiritualtablets.adapters.EventAdapter;
import com.example.spiritualtablets.models.EventItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsByLanguageActivity extends AppCompatActivity {

    Spinner languageSpinner;
    String selectedLanguage;
    RecyclerView eventsRV;
    private List<EventItem> eventItems;
    private EventAdapter eventAdapter;
    LoadingDialog loadingDialog;
    DatabaseReference user_admin_ref;
    String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_by_language);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final FloatingActionButton addEventsButton = findViewById(R.id.add_events_button);

        user_admin_ref = FirebaseDatabase.getInstance().getReference("Users").child(current_user_id).child("admin");

        user_admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    addEventsButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EventsByLanguageActivity.this, AddEventActivity.class));
            }
        });

        languageSpinner = findViewById(R.id.events_by_language);

        loadingDialog = new LoadingDialog(this);

        eventsRV = findViewById(R.id.events_by_language_recycler_view);
        eventsRV.setLayoutManager(new LinearLayoutManager(this));

        final String[] languages = {"Select Language", "English", "Telugu", "Hindi"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(languageAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedLanguage = languages[position];

                switch (selectedLanguage){

                    case "English":
                        loadingDialog.startLoading();
                        DatabaseReference english_event_ref = FirebaseDatabase.getInstance().getReference("events").child("English");
                        english_event_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    eventItems = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                        eventItems.add(eventItem);
                                    }
                                    eventAdapter = new EventAdapter(EventsByLanguageActivity.this, eventItems);
                                    eventsRV.setAdapter(eventAdapter);
                                    loadingDialog.dismiss();

                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(EventsByLanguageActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                    eventsRV.setAdapter(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;

                    case "Telugu":
                        loadingDialog.startLoading();
                        DatabaseReference telugu_event_ref = FirebaseDatabase.getInstance().getReference("events").child("Telugu");
                        telugu_event_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    eventItems = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                        eventItems.add(eventItem);
                                    }
                                    eventAdapter = new EventAdapter(EventsByLanguageActivity.this, eventItems);
                                    eventsRV.setAdapter(eventAdapter);
                                    loadingDialog.dismiss();

                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(EventsByLanguageActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                    eventsRV.setAdapter(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;
                    case "Hindi":
                        loadingDialog.startLoading();
                        DatabaseReference hindi_event_ref = FirebaseDatabase.getInstance().getReference("events").child("Hindi");
                        hindi_event_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    eventItems = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                        eventItems.add(eventItem);
                                    }
                                    eventAdapter = new EventAdapter(EventsByLanguageActivity.this, eventItems);
                                    eventsRV.setAdapter(eventAdapter);
                                    loadingDialog.dismiss();

                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(EventsByLanguageActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                    eventsRV.setAdapter(null);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}

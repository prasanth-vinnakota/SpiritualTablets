package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spiritualtablets.adapters.EventAdapter;
import com.example.spiritualtablets.models.EventItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsByLanguageActivity extends AppCompatActivity {

    Spinner languageSpinner;
    String selectedLanguage;
    RecyclerView eventsRV;
    private List<EventItem> eventItems;
    private EventAdapter eventAdapter;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_by_language);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        languageSpinner = findViewById(R.id.events_by_language);

        loadingDialog = new LoadingDialog(this);

        eventsRV = findViewById(R.id.events_by_language_recycler_view);
        eventsRV.setLayoutManager(new LinearLayoutManager(this));

        final String[] languages = {"Select Language", "English", "Telugu"};
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
                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
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
                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
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

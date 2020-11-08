package developer.prasanth.spiritualtablets;

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

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.adapters.EventAdapter;
import developer.prasanth.spiritualtablets.models.EventItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    Spinner countrySpinner, stateSpinner;
    String selectedCountry, selectedState;
    RecyclerView eventRV;
    private List<EventItem> eventItems;
    private EventAdapter eventAdapter;
    LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        countrySpinner = findViewById(R.id.country_spinner);
        stateSpinner = findViewById(R.id.state_spinner);

        loadingDialog = new LoadingDialog(this);

        eventRV = findViewById(R.id.events_recycler_view);
        eventRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final String[] countries = {"Select Country", "India", "United States"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, countries);
        countrySpinner.setAdapter(countryAdapter);


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCountry = countries[position];
                switch (selectedCountry) {
                    case "Select Country": {

                        String[] states = {"Select State"};
                        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(EventsActivity.this, R.layout.support_simple_spinner_dropdown_item, states);
                        stateSpinner.setAdapter(statesAdapter);
                        eventRV.setAdapter(null);
                        break;
                    }
                    case "India": {

                        final String[] states = {"Select State",
                                "Andhra Pradesh",
                                "Arunachal Pradesh",
                                "Assam",
                                "Bihar",
                                "Chandigarh",
                                "Chhattisgarh",
                                "Delhi",
                                "Goa",
                                "Gujarat",
                                "Haryana",
                                "Himachal Pradesh",
                                "Jammu and Kashmir",
                                "Jharkhand",
                                "Karnataka",
                                "Kerala",
                                "Madhya Pradesh",
                                "Maharashtra",
                                "Manipur",
                                "Meghalaya",
                                "Mizoram",
                                "Nagaland",
                                "Odisha",
                                "Punjab",
                                "Rajasthan",
                                "Sikkim",
                                "Tamil Naidu",
                                "Telangana",
                                "Tripura",
                                "Uttar Pradesh",
                                "Uttarakhand",
                                "West Bengal",
                        };
                        ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(EventsActivity.this, R.layout.support_simple_spinner_dropdown_item, states);
                        stateSpinner.setAdapter(statesAdapter);

                        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedState = states[position];

                                switch (selectedState) {
                                    case "Andhra Pradesh":
                                        loadingDialog.startLoading();
                                        DatabaseReference andhra_pradesh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Andhra Pradesh");
                                        andhra_pradesh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Arunachal Pradesh":
                                        loadingDialog.startLoading();
                                        DatabaseReference arunachal_pradesh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Arunachal Pradesh");
                                        arunachal_pradesh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Assam":
                                        loadingDialog.startLoading();
                                        DatabaseReference assam_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Assam");
                                        assam_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Bihar":
                                        loadingDialog.startLoading();
                                        DatabaseReference bihar_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Bihar");
                                        bihar_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Chandigarh":
                                        loadingDialog.startLoading();
                                        DatabaseReference chandigarh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Chandigarh");
                                        chandigarh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Chhattisgarh":
                                        loadingDialog.startLoading();
                                        DatabaseReference chhattisgarh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Chhattisgarh");
                                        chhattisgarh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Delhi":
                                        loadingDialog.startLoading();
                                        DatabaseReference delhi_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Delhi");
                                        delhi_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Goa":
                                        loadingDialog.startLoading();
                                        DatabaseReference goa_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Goa");
                                        goa_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Gujarat":
                                        loadingDialog.startLoading();
                                        DatabaseReference gujarat_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Gujarat");
                                        gujarat_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Haryana":
                                        loadingDialog.startLoading();
                                        DatabaseReference haryana_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Haryana");
                                        haryana_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Himachal Pradesh":
                                        loadingDialog.startLoading();
                                        DatabaseReference himachal_pradesh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Himachal Pradesh");
                                        himachal_pradesh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Jammu and Kashmir":
                                        loadingDialog.startLoading();
                                        DatabaseReference jammu_and_kashmir_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Jammu and Kashmir");
                                        jammu_and_kashmir_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Jharkhand":
                                        loadingDialog.startLoading();
                                        DatabaseReference jharkhand_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Jharkhand");
                                        jharkhand_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Karnataka":
                                        loadingDialog.startLoading();
                                        DatabaseReference karnataka_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Karnataka");
                                        karnataka_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Kerala":
                                        loadingDialog.startLoading();
                                        DatabaseReference kerala_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Kerala");
                                        kerala_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Madhya Pradesh":
                                        loadingDialog.startLoading();
                                        DatabaseReference madhya_pradesh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Madhya Pradesh");
                                        madhya_pradesh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Maharashtra":
                                        loadingDialog.startLoading();
                                        DatabaseReference maharashtra_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Maharashtra");
                                        maharashtra_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Manipur":
                                        loadingDialog.startLoading();
                                        DatabaseReference manipur_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Manipur");
                                        manipur_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Meghalaya":
                                        loadingDialog.startLoading();
                                        DatabaseReference meghalaya_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Meghalaya");
                                        meghalaya_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Mizoram":
                                        loadingDialog.startLoading();
                                        DatabaseReference mizoram_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Mizoram");
                                        mizoram_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Nagaland":
                                        loadingDialog.startLoading();
                                        DatabaseReference nagaland_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Nagaland");
                                        nagaland_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Odisha":

                                        loadingDialog.startLoading();
                                        DatabaseReference odisha_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Odisha");
                                        odisha_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Punjab":
                                        loadingDialog.startLoading();
                                        DatabaseReference punjab_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Punjab");
                                        punjab_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Rajasthan":
                                        loadingDialog.startLoading();
                                        DatabaseReference rajasthan_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Rajasthan");
                                        rajasthan_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;


                                    case "Sikkim":
                                        loadingDialog.startLoading();
                                        DatabaseReference sikkim_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Sikkim");
                                        sikkim_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Tamil Naidu":
                                        loadingDialog.startLoading();
                                        DatabaseReference tamil_naidu_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Tamil Naidu");
                                        tamil_naidu_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Telangana":

                                        loadingDialog.startLoading();
                                        DatabaseReference telangana_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Telangana");
                                        telangana_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Tripura":
                                        loadingDialog.startLoading();
                                        DatabaseReference tripura_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Tripura");
                                        tripura_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Uttar Pradesh":
                                        loadingDialog.startLoading();
                                        DatabaseReference uttar_pradesh_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Uttar Pradesh");
                                        uttar_pradesh_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "Uttarakhand":
                                        loadingDialog.startLoading();
                                        DatabaseReference uttarakhand_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("Uttarakhand");
                                        uttarakhand_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        break;

                                    case "West Bengal":
                                        loadingDialog.startLoading();
                                        DatabaseReference west_bengal_event_ref = FirebaseDatabase.getInstance().getReference("events").child("India").child("West Bengal");
                                        west_bengal_event_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.exists()) {
                                                    eventItems = new ArrayList<>();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                        eventItems.add(eventItem);
                                                    }
                                                    eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                    eventRV.setAdapter(eventAdapter);
                                                    loadingDialog.dismiss();

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                    eventRV.setAdapter(null);
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

                        eventRV.setAdapter(null);
                        break;
                    }


                    case "United States": {

                        final String[] states = {"Select State", "California"};
                        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(EventsActivity.this, R.layout.support_simple_spinner_dropdown_item, states);
                        stateSpinner.setAdapter(statesAdapter);

                        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedState = states[position];
                                if (selectedState.equals("California")) {

                                    loadingDialog.startLoading();
                                    DatabaseReference california_event_ref = FirebaseDatabase.getInstance().getReference("events").child("United States").child("California");
                                    california_event_ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()) {
                                                eventItems = new ArrayList<>();
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    EventItem eventItem = dataSnapshot.getValue(EventItem.class);
                                                    eventItems.add(eventItem);
                                                }
                                                eventAdapter = new EventAdapter(getApplicationContext(), eventItems);
                                                eventRV.setAdapter(eventAdapter);
                                                loadingDialog.dismiss();

                                            } else {
                                                loadingDialog.dismiss();
                                                Toast.makeText(EventsActivity.this, "No events are currently running", Toast.LENGTH_SHORT).show();
                                                eventRV.setAdapter(null);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        eventRV.setAdapter(null);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AdminPanelActivity extends AppCompatActivity implements LatestEventsDialog.LatestEventListener {

    DatabaseReference latestEventsReference;
    DatabaseReference youtubeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdminPanelActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdminPanelActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        init();
    }

    private void init() {
        latestEventsReference = FirebaseDatabase.getInstance().getReference().child("latest_events");
        youtubeReference = FirebaseDatabase.getInstance().getReference("youtube");
    }

    public void adminPanelAddEvent(View view) {

        startActivity(new Intent(AdminPanelActivity.this, AddEventActivity.class));
        finish();
    }

    public void adminPanelAddScrollingEvent(View view) {

        LatestEventsDialog latestEventsDialog = new LatestEventsDialog();
        latestEventsDialog.show(getSupportFragmentManager(), "Add event");
    }

    @Override
    public void applyEventsTexts(final String event_name, final String event_link) {

        if (!TextUtils.isEmpty(event_name)) {

            latestEventsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    latestEventsReference.child("value").setValue(event_name)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (!TextUtils.isEmpty(event_link))
                                        latestEventsReference.child("link").setValue(event_link);
                                    showMessage("Event Added Successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    showMessage(e.getMessage());
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else {

            showMessage("Event Name Must Not Be Empty");
        }
    }

    public void adminPanelViewVolunteer(View view) {

        LayoutInflater inflater = LayoutInflater.from(AdminPanelActivity.this);
        View view1 = inflater.inflate(R.layout.view_volunteer_dialog, null);
        Button checkedVolunteers = view1.findViewById(R.id.view_checked_volunteers);
        Button unCheckedVolunteers = view1.findViewById(R.id.view_unchecked_volunteers);
        Button allVolunteers = view1.findViewById(R.id.view_all_volunteers);
        Button cancel = view1.findViewById(R.id.view_volunteers_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(AdminPanelActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        checkedVolunteers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminPanelActivity.this, VolunteersListActivity.class);
                intent.putExtra("type", "checked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        unCheckedVolunteers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminPanelActivity.this, VolunteersListActivity.class);
                intent.putExtra("type", "unchecked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        allVolunteers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminPanelActivity.this, VolunteersListActivity.class);
                intent.putExtra("type", "all");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void adminPanelViewRapidRegisteredPatients(View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(AdminPanelActivity.this);
        View view1 = layoutInflater.inflate(R.layout.participate_view_dialog, null);
        Button checkedPatients = view1.findViewById(R.id.view_checked_patients);
        Button uncheckedPatients = view1.findViewById(R.id.view_unchecked_patients);
        Button allPatients = view1.findViewById(R.id.view_all_patients);
        Button cancel = view1.findViewById(R.id.view_patients_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(AdminPanelActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        checkedPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RapidRegistrationListActivity.class);
                intent.putExtra("type", "checked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        uncheckedPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RapidRegistrationListActivity.class);
                intent.putExtra("type", "unchecked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        allPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RapidRegistrationListActivity.class);
                intent.putExtra("type", "all");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void adminPanelViewRequestForCounselling(View view) {

        LayoutInflater layoutInflater = LayoutInflater.from(AdminPanelActivity.this);
        View view1 = layoutInflater.inflate(R.layout.participate_view_dialog, null);
        Button checkedPatients = view1.findViewById(R.id.view_checked_patients);
        Button uncheckedPatients = view1.findViewById(R.id.view_unchecked_patients);
        Button allPatients = view1.findViewById(R.id.view_all_patients);
        Button cancel = view1.findViewById(R.id.view_patients_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(AdminPanelActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        checkedPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RequestForCounsellingListActivity.class);
                intent.putExtra("type", "checked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        uncheckedPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RequestForCounsellingListActivity.class);
                intent.putExtra("type", "unchecked");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        allPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, RequestForCounsellingListActivity.class);
                intent.putExtra("type", "all");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    private void showMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }
}
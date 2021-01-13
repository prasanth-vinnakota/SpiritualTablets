package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
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

public class DashBoardActivity extends AppCompatActivity implements LatestEventsDialog.LatestEventListener {

    private TextView marquee;
    private TextView counselor;
    private DatabaseReference userAdminReference;
    private DatabaseReference latestEventsReference;
    private DatabaseReference updatedReference;
    private DatabaseReference userReference;
    private String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private int VERSION_CODE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(DashBoardActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(DashBoardActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference volunteer_ref = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("registered_as_volunteer");
        volunteer_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Button button = findViewById(R.id.volunteer_button);
                    button.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference versionReference = FirebaseDatabase.getInstance().getReference("app_version").child("current_version");

        versionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        int version = Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString());
                        if (version != VERSION_CODE) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

                            builder.setTitle("App update required");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=developer.prasanth.spiritualtablets"));
                                    dialogInterface.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Intent a = new Intent(Intent.ACTION_MAIN);
                                    a.addCategory(Intent.CATEGORY_HOME);
                                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    dialog.dismiss();
                                    startActivity(a);
                                    finish();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } catch (Exception e) {
                        showMessage(e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userAdminReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("admin");

        userAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    MaterialCardView cardView = findViewById(R.id.admin_panel_parent);
                    cardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userReference = FirebaseDatabase.getInstance().getReference("users");

        userReference.child(currentUserId).child("settings").removeValue();
        userReference.child(currentUserId).child("user_state").removeValue();


        userReference.child(currentUserId).child("counselor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    userReference.child(Objects.requireNonNull(snapshot.getValue()).toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {

                                String name = "Your Counselor is " + Objects.requireNonNull(snapshot.child("full_name").getValue()).toString();
                                counselor.setText(name);
                                counselor.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        latestEventsReference = FirebaseDatabase.getInstance().getReference().child("latest_events");

        latestEventsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String one;
                    if (snapshot.exists()) {

                        one = Objects.requireNonNull(snapshot.child("value").getValue()).toString();
                        marquee.setVisibility(View.VISIBLE);
                        marquee.setText(one);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAdminReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

                            builder.setNegativeButton("Go to link", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    latestEventsReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("link").exists()) {

                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(Objects.requireNonNull(snapshot.child("link").getValue()).toString()));
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            });

                            builder.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    LatestEventsDialog latestEventsDialog = new LatestEventsDialog();
                                    latestEventsDialog.show(getSupportFragmentManager(), "Add event");
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        } else {

                            latestEventsReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.child("link").exists()) {

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(Objects.requireNonNull(snapshot.child("link").getValue()).toString()));
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    private void init(){

        marquee = findViewById(R.id.dashboard_marquee);
        marquee.setSelected(true);

        counselor = findViewById(R.id.dashboard_counselor);
        counselor.setSelected(true);

        TextView volunteer = findViewById(R.id.dashboard_volunteer);
        volunteer.setSelected(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!checkInternetConnection()) {
            startActivity(new Intent(this, NoInternetActivity.class));
            return;
        }

        updatedReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);


        updatedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.child("updated").exists()) {

                    updatedReference.child("books").setValue("no");
                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                    builder.setMessage("You Need To Update Your Profile");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(DashBoardActivity.this, ProfileActivity.class));
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public boolean checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        if (connectivityManager != null) {

            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        return (mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected());
    }


    public void openBooks(View view) {

        LayoutInflater inflater = LayoutInflater.from(DashBoardActivity.this);

        View view1 = inflater.inflate(R.layout.language_dialog, null);

        Button telugu = view1.findViewById(R.id.telugu_books);
        Button english = view1.findViewById(R.id.english_books);
        Button hindi = view1.findViewById(R.id.hindi_books);
        Button kannada = view1.findViewById(R.id.kannada_books);
        Button cancel = view1.findViewById(R.id.alert_language_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(DashBoardActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, TeluguBookListActivity.class));
                dialog.dismiss();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, EnglishBookListActivity.class));
                dialog.dismiss();
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, HindiBookListActivity.class));
                dialog.dismiss();
            }
        });

        kannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, KannadaBookListActivity.class));
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void playMusic(View view) {

        startActivity(new Intent(DashBoardActivity.this, AudioActivity.class));
    }

    public void loadVideo(View view) {

        LayoutInflater inflater = LayoutInflater.from(DashBoardActivity.this);

        View view1 = inflater.inflate(R.layout.language_dialog, null);

        Button telugu = view1.findViewById(R.id.telugu_books);
        Button english = view1.findViewById(R.id.english_books);
        Button hindi = view1.findViewById(R.id.hindi_books);
        Button kannada = view1.findViewById(R.id.kannada_books);
        kannada.setVisibility(View.GONE);
        Button cancel = view1.findViewById(R.id.alert_language_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(DashBoardActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, VideoListActivity.class);
                intent.putExtra("language", "telugu");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, VideoListActivity.class);
                intent.putExtra("language", "english");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, VideoListActivity.class);
                intent.putExtra("language", "hindi");
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


    public void gallery(View view) {

        startActivity(new Intent(this, GalleryActivity.class));
    }

    public void newsLetters(View view) {

        startActivity(new Intent(this, NewsLettersActivity.class));
    }

    public void about(View view) {

        startActivity(new Intent(this, AboutUsActivity.class));
    }

    public void donate(View view) {
        showMessage("Loading please wait...");

        DatabaseReference booksReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("books");

        booksReference.setValue("yes")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.payumoney.com/react/app/merchant/#/pay/merchant/A381DF3EC1177559CC7B5B2440F3DC67?param=7102505"));
                startActivity(intent);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Changing Books Field In Current User Id Is Failed");
            }
        });
    }

    public void contactUs(View view) {

        startActivity(new Intent(this, ContactUsActivity.class));
    }

    public void dailyTip(View view) {

        startActivity(new Intent(this, DailyQuoteActivity.class));
    }

    public void registerParticipate(View view) {

        View dialog_view = getLayoutInflater().inflate(R.layout.participate_registration_dialog, null);

        Button rapid_registration = dialog_view.findViewById(R.id.rapid_registration);
        Button request_for_counselling = dialog_view.findViewById(R.id.request_for_counselling);
        Button cancel = dialog_view.findViewById(R.id.alert_patient_registration_cancel);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(dialog_view)
                .create();
        alertDialog.show();

        rapid_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, RapidRegistrationActivity.class));
                alertDialog.dismiss();
            }
        });

        request_for_counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, RequestForCounsellingActivity.class));
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void registerVolunteer(View view) {

        startActivity(new Intent(this, VolunteerRegistrationActivity.class));
    }

    public void events(View view) {

        LayoutInflater inflater = LayoutInflater.from(DashBoardActivity.this);

        View view1 = inflater.inflate(R.layout.language_dialog, null);

        Button telugu = view1.findViewById(R.id.telugu_books);
        Button english = view1.findViewById(R.id.english_books);
        Button hindi = view1.findViewById(R.id.hindi_books);
        Button kannada = view1.findViewById(R.id.kannada_books);
        kannada.setText(R.string.others);
        Button cancel = view1.findViewById(R.id.alert_language_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(DashBoardActivity.this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, EventsByLanguageActivity.class);
                intent.putExtra("language", "Telugu");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, EventsByLanguageActivity.class);
                intent.putExtra("language", "English");
                startActivity(intent);
                dialog.dismiss();
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, EventsByLanguageActivity.class);
                intent.putExtra("language", "Hindi");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        kannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, EventsByLanguageActivity.class);
                intent.putExtra("language", "Others");
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

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoggedInActivity.class));
        finish();
    }

    public void settings(View view) {

        Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openAdminPanel(View view) {
        startActivity(new Intent(DashBoardActivity.this, AdminPanelActivity.class));
        finish();
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
                            marquee.setText(event_name);
                            if (!TextUtils.isEmpty(event_link)) {
                                latestEventsReference.child("link").setValue(event_link);
                            }
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
    private void showMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.create().show();
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void shareApp(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = "Download this Application now:-https://play.google.com/store/apps/details?id=developer.prasanth.spiritualtablets&hl=en";
        String subject = "Spiritual Tablets App";
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent,"Share Using"));
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}

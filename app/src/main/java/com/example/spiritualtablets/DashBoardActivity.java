package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiritualtablets.chat_bot.ChatBotActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DashBoardActivity extends AppCompatActivity {

    TextView marquee1, marquee2;
    DatabaseReference user_admin_ref, latest_events_ref;
    String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        marquee1 = findViewById(R.id.dashboard_marquee_1);
        marquee1.setSelected(true);

        marquee2 = findViewById(R.id.dashboard_marquee_2);
        marquee2.setSelected(true);

        user_admin_ref = FirebaseDatabase.getInstance().getReference("Users").child(current_user_id).child("admin");

        latest_events_ref = FirebaseDatabase.getInstance().getReference().child("latest_events");

        latest_events_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String one = "", two = "";
                    if (snapshot.child("1").exists()){
                        one = Objects.requireNonNull(snapshot.child("1").getValue()).toString();
                        marquee1.setVisibility(View.VISIBLE);
                        marquee1.setText(one);

                    }

                    if (snapshot.child("2").exists()) {
                        two = Objects.requireNonNull(snapshot.child("2").getValue()).toString();
                        marquee2.setVisibility(View.VISIBLE);
                        marquee2.setText(two);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        marquee1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_admin_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

                            builder.setTitle("Enter description of the event");

                            final EditText event_description = new EditText(DashBoardActivity.this);
                            event_description.setPadding(10,10,10,10);

                            builder.setView(event_description);

                            builder.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    final String event = event_description.getText().toString();

                                    if (!TextUtils.isEmpty(event)) {

                                        latest_events_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    latest_events_ref.child("1").setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            marquee1.setText(event);
                                                            Toast.makeText(DashBoardActivity.this, "event added successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(DashBoardActivity.this, "Event description must not be empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        marquee2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_admin_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

                            builder.setTitle("Enter description of the event");

                            final EditText event_description = new EditText(DashBoardActivity.this);
                            event_description.setPadding(10,10,10,10);

                            builder.setView(event_description);

                            builder.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    final String event = event_description.getText().toString();

                                    if (!TextUtils.isEmpty(event)) {

                                        latest_events_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    latest_events_ref.child("2").setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            marquee2.setText(event);
                                                            Toast.makeText(DashBoardActivity.this, "event added successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(DashBoardActivity.this, "Event description must not be empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void openBooks(View view) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View view1 = inflater.inflate(R.layout.language_dialog, null);

        Button telugu = view1.findViewById(R.id.telugu_books);
        Button english = view1.findViewById(R.id.english_books);
        Button hindi = view1.findViewById(R.id.hindi_books);
        Button kannada = view1.findViewById(R.id.kannada_books);
        Button cancel = view1.findViewById(R.id.alert_language_cancel);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(view1)
                .create();

        dialog.show();

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this, TeluguBookListActivity.class));
                loadingDialog.dismiss();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this, EnglishBookListActivity.class));
                loadingDialog.dismiss();
            }
        });

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this,HindiBookListActivity.class));
                loadingDialog.dismiss();
            }
        });

        kannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this,KannadaBookListActivity.class));
                loadingDialog.dismiss();
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

       /* AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

        builder.setTitle("No Videos Available");
        AlertDialog dialog = builder.create();
        dialog.show();*/

        startActivity(new Intent(this, VideoListActivity.class));
    }

    public boolean checkInternetConnection() {

        //initialize connectivityManager to get the statuses of connectivity services
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        //connectivityManager have statuses of connection services
        if (connectivityManager != null) {

            //get the status of mobile data
            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //get status of wifi
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        //mobile data or wifi is connected
        //exit
        return (mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkInternetConnection())
            startActivity(new Intent(this, NoInternetActivity.class));
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
        Toast.makeText(this, "Loading please wait...", Toast.LENGTH_LONG).show();

        String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("Users").child(current_user_id).child("books");
        books_ref.setValue("yes").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.payumoney.com/react/app/merchant/#/pay/merchant/A381DF3EC1177559CC7B5B2440F3DC67?param=7102505"));
                startActivity(intent);
            }
        });


    }

    public void contactUs(View view) {

        startActivity(new Intent(this, ContactUsActivity.class));
    }

    public void dailyTip(View view) {

        startActivity(new Intent(this, DailyTipActivity.class));
    }

    public void chat(View view) {

        startActivity(new Intent(this, ChatMainActivity.class));
    }

    public void registerPatient(View view) {

        View dialog_view = getLayoutInflater().inflate(R.layout.patient_registration_dialog, null);

        Button detail_registration = dialog_view.findViewById(R.id.detail_registration);
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
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://spiritualtablet.org/RapidRegistration.html")));
                alertDialog.dismiss();
            }
        });

        detail_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://spiritualtablet.org/PatientRegistration.html")));
                alertDialog.dismiss();
            }
        });

        request_for_counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("Users").child(current_user_id).child("books");
                books_ref.setValue("yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://spiritualtablet.org/RequestForCounseling.html")));
                        alertDialog.dismiss();
                    }
                });
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

        startActivity(new Intent(this, EventsByLanguageActivity.class));
    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoggedInActivity.class));
        finish();
    }

    public void settings(View view) {

        startActivity(new Intent(DashBoardActivity.this, SettingsActivity.class));
    }

    public void fab(View view) {
        startActivity(new Intent(DashBoardActivity.this, ChatBotActivity.class));
    }
}

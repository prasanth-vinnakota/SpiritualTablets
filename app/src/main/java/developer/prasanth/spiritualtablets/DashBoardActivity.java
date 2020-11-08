package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.chat_bot.ChatBotActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class DashBoardActivity extends AppCompatActivity implements LatestEventsDialog.LatestEventListener {

    private TextView marquee,counselor,volunteer;
    private DatabaseReference user_admin_ref, latest_events_ref, updated_ref, uid_ref, user_ref, counselors_ref, work_ref;
    private String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private int REQUEST_CODE = 1;
    private boolean counselor_boolean;
    private MaterialCardView add_spiritual_children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        marquee = findViewById(R.id.dashboard_marquee);
        marquee.setSelected(true);

        counselor = findViewById(R.id.dashboard_counselor);
        counselor.setSelected(true);

        volunteer = findViewById(R.id.dashboard_volunteer);
        volunteer.setSelected(true);

        add_spiritual_children = findViewById(R.id.add_spiritual_children_parent);

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(DashBoardActivity.this);

        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){

                    try {
                        appUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,DashBoardActivity.this,REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        counselors_ref = FirebaseDatabase.getInstance().getReference("counselors");
        counselors_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    counselor_boolean = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (Objects.equals(dataSnapshot.getKey(), current_user_id)){
                            counselor_boolean = true;
                            break;
                        }
                    }

                    if (counselor_boolean)
                        add_spiritual_children.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        work_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("work");
        work_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   DatabaseReference number_ref = FirebaseDatabase.getInstance().getReference("work").child(Objects.requireNonNull(snapshot.getValue()).toString());
                   number_ref.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               String s = "Your Coordinator Number Is " + Objects.requireNonNull(snapshot.getValue()).toString();
                               volunteer.setText(s);
                               volunteer.setVisibility(View.VISIBLE);
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


        user_admin_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("admin");
        user_admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    MaterialCardView cardView = findViewById(R.id.admin_panel_parent);
                    cardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user_ref = FirebaseDatabase.getInstance().getReference("users");
        user_ref.child(current_user_id).child("mobile_no").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    uid_ref = FirebaseDatabase.getInstance().getReference().child("uid").child(Objects.requireNonNull(snapshot.getValue()).toString());

                    uid_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                uid_ref.setValue(current_user_id);
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

        user_ref.child(current_user_id).child("counselor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    user_ref.child(Objects.requireNonNull(snapshot.getValue()).toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                String name = "Your Counselor is "+Objects.requireNonNull(snapshot.child("full_name").getValue()).toString();
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

        latest_events_ref = FirebaseDatabase.getInstance().getReference().child("latest_events");

        latest_events_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String one = "", two = "";
                    if (snapshot.exists()){
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
                user_admin_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

                            builder.setNegativeButton("Go to link", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    latest_events_ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.child("link").exists()){

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
                                    latestEventsDialog.show(getSupportFragmentManager(),"Add event");
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                        else {
                            latest_events_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child("link").exists()){

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

    public void onBackPressed() {
        updateUserStatus("offline");
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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

        updateUserStatus("online");

        updated_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id);


        updated_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("updated").exists()){
                    updated_ref.child("account_created_time").setValue(ServerValue.TIMESTAMP);
                    updated_ref.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    updated_ref.child("books").setValue("no");
                    updated_ref.child("settings").child("mobile_number").setValue("false");
                    updated_ref.child("settings").child("dob").setValue("false");
                    updated_ref.child("settings").child("profile_status").setValue("true");

                    startActivity(new Intent(DashBoardActivity.this, SettingsActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserStatus("online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus("offline");
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

        DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("books");
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
                startActivity(new Intent(DashBoardActivity.this, RapidRegistrationActivity.class));
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
                startActivity(new Intent(DashBoardActivity.this,RequestForCounsellingActivity.class));
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

        startActivity(new Intent(this, EventsByLanguageActivity.class));
    }

    public void logout(View view) {

        updateUserStatus("offline");

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

    public void findFriends(View view) {
        startActivity(new Intent(DashBoardActivity.this, FindFriendActivity.class));
    }



    private void updateUserStatus(String state){

        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineState = new HashMap<>();
        onlineState.put("time", saveCurrentTime);
        onlineState.put("date", saveCurrentDate);
        onlineState.put("state", state);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        rootRef.child(current_user_id).child("user_state")
                .updateChildren(onlineState);


    }

    public void requests(View view) {
        startActivity(new Intent(this, RequestsActivity.class));
    }

    public void contacts(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }



    public void openAdminPanel(View view) {
        startActivity(new Intent(DashBoardActivity.this, AdminPanelActivity.class));
        finish();
    }

    @Override
    public void applyEventsTexts(final String event_name, final String event_link) {
        if (!TextUtils.isEmpty(event_name)) {

            latest_events_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    latest_events_ref.child("value").setValue(event_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            marquee.setText(event_name);
                            if (!TextUtils.isEmpty(event_link)) {
                                latest_events_ref.child("link").setValue(event_link);
                            }
                            Toast.makeText(DashBoardActivity.this, "event added successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DashBoardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(DashBoardActivity.this, "Event name must not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void addSpiritualChildren() {
        final EditText editText = new EditText(DashBoardActivity.this);
        editText.setHint("+919100362607");
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setTitle("Enter Registered Mobile Number With Country Code");
        builder.setView(editText);
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(DashBoardActivity.this, "Mobile Number must not be empty", Toast.LENGTH_SHORT).show();
                }
                uid_ref = FirebaseDatabase.getInstance().getReference().child("uid").child(editText.getText().toString());
                uid_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            counselors_ref = FirebaseDatabase.getInstance().getReference("counselors").child(current_user_id).child(Objects.requireNonNull(snapshot.getValue()).toString());
                            counselors_ref.setValue(true);
                            user_ref = FirebaseDatabase.getInstance().getReference("users").child(snapshot.getValue().toString()).child("counselor");
                            user_ref.setValue(current_user_id);
                            dialogInterface.dismiss();
                            Toast.makeText(DashBoardActivity.this, "Child Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DashBoardActivity.this);
                            builder1.setTitle("Failed To Add Child");
                            builder1.setMessage("reasons may be:\n" +
                                    "1.please check the number is in format of +919100362607\n" +
                                    "2.please check the user registered with the mobile number you entered\n" +
                                    "3.user may not updated the app");

                            builder1.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.create().show();
    }

    public void viewSpiritualChildren(View view) {
    }

}
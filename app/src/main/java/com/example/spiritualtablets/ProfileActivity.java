package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference receiverUserRef, chatRequestRef, contactsRef;
    private String currentState, receiverUserID, senderUserID;
    private TextView userProfileName, userProfileStatus, userEmail, userMobileNo, userDob, userLocation;
    private CircleImageView userProfileImage;
    Button sendMessageRequestButton, acceptOrDeclineRequestButton;
    boolean mobile_number = false, dob = false, profile_status = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.visit_tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Retrieving user data");
        progressDialog.setMessage("please wait white we are retrieving user information");
        progressDialog.show();

        receiverUserID = getIntent().getStringExtra("user_id");
        senderUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        receiverUserRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUserID);
        chatRequestRef = FirebaseDatabase.getInstance().getReference("chat_requests");
        contactsRef = FirebaseDatabase.getInstance().getReference("contacts");

        userProfileStatus = findViewById(R.id.visit_profile_status);
        userProfileName = findViewById(R.id.visit_user_name);
        userEmail = findViewById(R.id.visit_email);
        userDob = findViewById(R.id.visit_dob);
        userMobileNo = findViewById(R.id.visit_mobile_number);
        userLocation = findViewById(R.id.visit_location);

        userProfileImage = findViewById(R.id.visit_profile_image);

        sendMessageRequestButton = findViewById(R.id.send_message_request_button);
        acceptOrDeclineRequestButton = findViewById(R.id.accept_or_decline_message_request_button);

        currentState = "new";

        retrieveUserInfo();
        progressDialog.dismiss();
    }

    private void retrieveUserInfo() {

        receiverUserRef.child("settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (Objects.requireNonNull(snapshot.child("dob").getValue()).toString().equals("true"))
                        dob = true;

                    if (Objects.requireNonNull(snapshot.child("profile_status").getValue()).toString().equals("true"))
                        profile_status = true;

                    if (Objects.requireNonNull(snapshot.child("mobile_number").getValue()).toString().equals("true"))
                        mobile_number = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        receiverUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Objects.requireNonNull(getSupportActionBar()).setTitle(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());

                    userProfileName.setText(Objects.requireNonNull(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString()));
                    String email = "@" + Objects.requireNonNull(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                    userEmail.setText(email);

                    String location = Objects.requireNonNull(snapshot.child("city").getValue()).toString() + "," + Objects.requireNonNull(snapshot.child("state").getValue()).toString() + "," + Objects.requireNonNull(snapshot.child("country").getValue()).toString();
                    userLocation.setText(location);

                    Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(userProfileImage);

                    if (dob){

                        userDob.setVisibility(View.VISIBLE);
                        userDob.setText(Objects.requireNonNull(Objects.requireNonNull(snapshot.child("date_of_birth").getValue()).toString()));
                    }

                    if (profile_status){

                        userProfileStatus.setVisibility(View.VISIBLE);
                        userProfileStatus.setText(Objects.requireNonNull(Objects.requireNonNull(snapshot.child("profile_status").getValue()).toString()));
                    }

                    if (mobile_number){

                        userMobileNo.setVisibility(View.VISIBLE);
                        userMobileNo.setText(Objects.requireNonNull(Objects.requireNonNull(snapshot.child("mobile_no").getValue()).toString()));
                    }

                    manageChatRequest();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void manageChatRequest() {

        if (!senderUserID.equals(receiverUserID)) {

            chatRequestRef.child(senderUserID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(receiverUserID)) {

                                String request_type = Objects.requireNonNull(snapshot.child(receiverUserID).child("request_type").getValue()).toString();
                                if (request_type.equals("received")) {

                                    currentState = "request_received";
                                    sendMessageRequestButton.setText(R.string.accept_request);
                                    acceptOrDeclineRequestButton.setVisibility(View.VISIBLE);
                                    acceptOrDeclineRequestButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelRequest();
                                        }
                                    });
                                } else if (request_type.equals("sent")) {

                                    sendMessageRequestButton.setEnabled(true);
                                    currentState = "request_sent";
                                    sendMessageRequestButton.setText(R.string.cancel_request);
                                }

                            } else {

                                contactsRef.child(senderUserID)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.hasChild(receiverUserID)){

                                                    currentState = "friends";
                                                    sendMessageRequestButton.setText(R.string.unfriend);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }});

                                contactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (!snapshot.hasChild(senderUserID)){
                                            sendMessageRequestButton.setEnabled(true);
                                            currentState = "new";
                                            sendMessageRequestButton.setText(R.string.send_request);
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


            sendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendMessageRequestButton.setEnabled(false);


                    if (currentState.equals("new")) {

                        sendRequest();
                    }
                    if (currentState.equals("request_sent")) {

                        cancelRequest();
                    }
                    if (currentState.equals("request_received")){
                        acceptRequest();
                    }
                    if (currentState.equals("friends")){

                        removeContact();
                    }
                }
            });
        } else {
            sendMessageRequestButton.setVisibility(View.GONE);
        }

    }

    private void removeContact() {

        contactsRef.child(senderUserID)
                .child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isComplete()){

                            contactsRef.child(receiverUserID)
                                    .child(senderUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isComplete()){

                                                currentState = "new";
                                                sendMessageRequestButton.setText(R.string.send_request);
                                                sendMessageRequestButton.setEnabled(true);

                                                acceptOrDeclineRequestButton.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void acceptRequest() {

        contactsRef.child(senderUserID)
                .child(receiverUserID)
                .child("contact")
                .setValue("saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isComplete()){

                            contactsRef.child(receiverUserID)
                                    .child(senderUserID)
                                    .child("contact")
                                    .setValue("saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isComplete()){

                                                chatRequestRef.child(senderUserID)
                                                        .child(receiverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                chatRequestRef.child(receiverUserID)
                                                                        .child(senderUserID)
                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isComplete()){

                                                                            sendMessageRequestButton.setEnabled(true);
                                                                            currentState = "friends";
                                                                            sendMessageRequestButton.setText(R.string.unfriend);
                                                                            acceptOrDeclineRequestButton.setVisibility(View.GONE);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelRequest() {

        chatRequestRef.child(senderUserID).child(receiverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        chatRequestRef.child(receiverUserID).child(senderUserID)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            sendMessageRequestButton.setEnabled(true);
                                            currentState = "new";
                                            sendMessageRequestButton.setText(R.string.send_request);

                                            acceptOrDeclineRequestButton.setVisibility(View.GONE);

                                            Toast.makeText(ProfileActivity.this, "Request canceled successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }

    private void sendRequest() {

        chatRequestRef.child(senderUserID).child(receiverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            chatRequestRef.child(receiverUserID).child(senderUserID)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                sendMessageRequestButton.setEnabled(true);
                                                currentState = "request_sent";
                                                sendMessageRequestButton.setText(R.string.cancel_request);

                                                Toast.makeText(ProfileActivity.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }
}

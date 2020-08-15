package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText userName, userStatus;
    private CircleImageView userProfileImage;
    FirebaseAuth mAuth;
    String currentUserId;
    DatabaseReference userRef;
    private static final int REQUEST_CODE = 1;
    private StorageReference mStorage;
    ProgressDialog progressDialog;
    SwitchCompat dobSwitch, mobileNumberSwitch, profileStatusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.settings_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.settings);

        dobSwitch = findViewById(R.id.show_dob_switch);
        mobileNumberSwitch = findViewById(R.id.show_mobile_number_switch);
        profileStatusSwitch = findViewById(R.id.show_profile_status_switch);

        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Profile is updating");
        progressDialog.setMessage("Please wait while we are updating your profile");
        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_profile_status);

        mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        userProfileImage = findViewById(R.id.set_profile_image);

        Button updateAccountSettings = findViewById(R.id.update_settings_button);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE);
            }
        });

        dobSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    userRef.child("settings").child("dob").setValue("true");
                } else {

                    userRef.child("settings").child("dob").setValue("false");
                }
            }
        });

        mobileNumberSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    userRef.child("settings").child("mobile_number").setValue("true");
                } else {

                    userRef.child("settings").child("mobile_number").setValue("false");
                }
            }
        });

        profileStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    userRef.child("settings").child("profile_status").setValue("true");
                } else {

                    userRef.child("settings").child("profile_status").setValue("false");
                }
            }
        });

        initFields();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(SettingsActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                final Uri resultUri = result.getUri();
                progressDialog.show();
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(resultUri)
                        .build();

                Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        final StorageReference filePath = mStorage.child(currentUserId + ".jpg");

                        filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        userRef.child("image").setValue(uri.toString());

                                        progressDialog.dismiss();
                                        initFields();
                                        Toast.makeText(SettingsActivity.this, "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                progressDialog.dismiss();
                Exception exception = result.getError();
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initFields() {
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("full_name").getValue() != null)
                        userName.setText(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());

                    if (snapshot.child("profile_status").getValue() != null)
                        userStatus.setText(Objects.requireNonNull(snapshot.child("profile_status").getValue()).toString());

                    if (snapshot.child("image").getValue() != null)
                        Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).placeholder(R.drawable.user_photo).into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child("settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (Objects.requireNonNull(snapshot.child("dob").getValue()).toString().equals("true")) {

                        dobSwitch.setChecked(true);
                    } else {
                        dobSwitch.setChecked(false);
                    }

                    if (Objects.requireNonNull(snapshot.child("mobile_number").getValue()).toString().equals("true")) {

                        mobileNumberSwitch.setChecked(true);
                    } else {
                        mobileNumberSwitch.setChecked(false);
                    }

                    if (Objects.requireNonNull(snapshot.child("profile_status").getValue()).toString().equals("true")) {

                        profileStatusSwitch.setChecked(true);
                    } else {
                        profileStatusSwitch.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateSettings() {

        String user_name = userName.getText().toString();
        String user_status = userStatus.getText().toString();

        if (TextUtils.isEmpty(user_name)) {
            userName.setError("must not be empty");
            return;
        }

        if (TextUtils.isEmpty(user_status)) {
            userName.setError("must not be empty");
            return;
        }

        progressDialog.show();

        final Map<String, Object> profileMap = new HashMap<>();

        profileMap.put("full_name", user_name);
        profileMap.put("profile_status", user_status);

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(user_name)
                .build();

        Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    userRef.updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Profile updates successfully..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                initFields();
                            } else {
                                Toast.makeText(SettingsActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }
}

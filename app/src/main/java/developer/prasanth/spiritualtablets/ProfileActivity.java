package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText userName;
    private TextInputEditText userStatus;
    private TextInputEditText userDob;
    private TextInputEditText userCountry;
    private TextInputEditText userState;
    private TextInputEditText userCity;
    private CircleImageView userProfileImage;
    FirebaseAuth firebaseAuth;
    String currentUserId;
    DatabaseReference userReference;
    private static final int REQUEST_CODE = 1;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    SwitchCompat dobSwitch, mobileNumberSwitch, profileStatusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

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

                    userReference.child("settings").child("dob").setValue("true");
                } else {

                    userReference.child("settings").child("dob").setValue("false");
                }
            }
        });

        mobileNumberSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    userReference.child("settings").child("mobile_number").setValue("true");
                } else {

                    userReference.child("settings").child("mobile_number").setValue("false");
                }
            }
        });

        profileStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    userReference.child("settings").child("profile_status").setValue("true");
                } else {

                    userReference.child("settings").child("profile_status").setValue("false");
                }
            }
        });

        retrieveUserDataFromDatabase();
    }

    private void init() {

        dobSwitch = findViewById(R.id.show_dob_switch);
        mobileNumberSwitch = findViewById(R.id.show_mobile_number_switch);
        profileStatusSwitch = findViewById(R.id.show_profile_status_switch);

        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_user_profile_status);
        userDob = findViewById(R.id.set_user_dob);
        userCountry = findViewById(R.id.set_user_country);
        userState = findViewById(R.id.set_user_state);
        userCity = findViewById(R.id.set_user_city);
        userProfileImage = findViewById(R.id.set_profile_image);

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Profile is updating");
        progressDialog.setMessage("Please wait while we are updating your profile");
        progressDialog.setCancelable(false);


        TextInputEditText userMobileNumber = findViewById(R.id.set_user_mobile_number);
        TextInputLayout user_mobile_no_parent = findViewById(R.id.profile_mobile_number_parent);

        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber() != null) {

            userMobileNumber.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());
            user_mobile_no_parent.setVisibility(View.VISIBLE);
        }

        TextInputEditText userEmail = findViewById(R.id.profile_email);
        TextInputLayout user_email_parent = findViewById(R.id.profile_email_parent);

        if (!Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).isEmpty()) {

            userEmail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
            user_email_parent.setVisibility(View.VISIBLE);
        }

        storageReference = FirebaseStorage.getInstance().getReference().child("user_photos");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        userReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(ProfileActivity.this);

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

                Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                final StorageReference filePath = storageReference.child(currentUserId + ".jpg");

                                filePath.putFile(resultUri)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                filePath.getDownloadUrl()
                                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {

                                                                userReference.child("image").setValue(uri.toString());

                                                                progressDialog.dismiss();
                                                                retrieveUserDataFromDatabase();
                                                                showMessage("Profile Image Uploaded Successfully");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                showMessage(e.getMessage());
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                showMessage(e.getMessage());
                                            }
                                        });
                            }
                        });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                assert result != null;
                progressDialog.dismiss();
                Exception exception = result.getError();
                showMessage(exception.getMessage());
            }
        }
    }

    private void retrieveUserDataFromDatabase() {
        userReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("full_name").getValue() != null)
                        userName.setText(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());

                    if (snapshot.child("profile_status").getValue() != null)
                        userStatus.setText(Objects.requireNonNull(snapshot.child("profile_status").getValue()).toString());

                    if (snapshot.child("date_of_birth").getValue() != null)
                        userDob.setText(Objects.requireNonNull(snapshot.child("date_of_birth").getValue()).toString());

                    if (snapshot.child("country").getValue() != null)
                        userCountry.setText(Objects.requireNonNull(snapshot.child("country").getValue()).toString());

                    if (snapshot.child("state").getValue() != null)
                        userState.setText(Objects.requireNonNull(snapshot.child("state").getValue()).toString());

                    if (snapshot.child("city").getValue() != null)
                        userCity.setText(Objects.requireNonNull(snapshot.child("city").getValue()).toString());

                    if (snapshot.child("image").getValue() != null)
                        Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).placeholder(R.drawable.user_photo).into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showMessage(error.getMessage());
            }
        });

        userReference.child("settings").addValueEventListener(new ValueEventListener() {
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

    public void updateSettings(View view) {

        String user_name = Objects.requireNonNull(userName.getText()).toString();
        String user_status = Objects.requireNonNull(userStatus.getText()).toString();
        String user_dob = Objects.requireNonNull(userDob.getText()).toString();
        String user_country = Objects.requireNonNull(userCountry.getText()).toString();
        String user_state = Objects.requireNonNull(userState.getText()).toString();
        String user_city = Objects.requireNonNull(userCity.getText()).toString();

        if (TextUtils.isEmpty(user_name)) {

            userName.setError("must not be empty");
            return;
        }

        if (TextUtils.isEmpty(user_status)) {

            userState.setError("must not be empty");
            return;
        }


        if (TextUtils.isEmpty(user_dob)) {

            userDob.setError("must not be empty");
            return;
        }

        if (TextUtils.isEmpty(user_country)) {

            userCountry.setError("must not be empty");
            return;
        }

        if (TextUtils.isEmpty(user_state)) {

            userState.setError("must not be empty");
            return;
        }

        if (TextUtils.isEmpty(user_city)) {

            userCity.setError("must not be empty");
            return;
        }

        progressDialog.show();
        userReference.child("updated").setValue("true");

        final Map<String, Object> profileMap = new HashMap<>();

        profileMap.put("full_name", user_name);
        profileMap.put("profile_status", user_status);
        profileMap.put("date_of_birth", user_dob);
        profileMap.put("country", user_country);
        profileMap.put("state", user_state);
        profileMap.put("city", user_city);

        final UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(user_name)
                .build();

        Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            userReference.updateChildren(profileMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                showMessage("Profile Updates Successfully");
                                                progressDialog.dismiss();
                                                retrieveUserDataFromDatabase();
                                            } else {

                                                showMessage(Objects.requireNonNull(task.getException()).getMessage());
                                                progressDialog.dismiss();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            showMessage(e.getMessage());
                                            progressDialog.dismiss();
                                        }
                                    });
                        } else {

                            showMessage(Objects.requireNonNull(task.getException()).getMessage());
                            progressDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showMessage(e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }

    private void showMessage(String message) {

        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}

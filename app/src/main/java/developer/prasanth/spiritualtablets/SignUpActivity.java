package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Button login, signUp;
    ImageView signUpImage, imgUserPhoto;
    TextView signUpText, signUpSlogan, signUpDob;
    TextInputEditText signUpFirstName, signUpLastName, signUpEmail, signUpMobileNumber, signUpPassword, signUpCountryCode, signUpCountry, signUpState, signUpCity;
    private FirebaseAuth mAuth;
    static int REQUEST_CODE = 1;
    Uri pickedImageUrl;
    ProgressDialog progressDialog;
    String mobileNumberWithCountryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        login = findViewById(R.id.sign_up_login);
        signUp = findViewById(R.id.sign_up);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while we are creating your account..");
        progressDialog.setCancelable(false);

        signUpImage = findViewById(R.id.sign_up_image);
        imgUserPhoto = findViewById(R.id.user_image);
        signUpSlogan = findViewById(R.id.sign_up_slogan);

        mAuth = FirebaseAuth.getInstance();

        signUpFirstName = findViewById(R.id.sign_up_first_name);
        signUpLastName = findViewById(R.id.sign_up_last_name);
        signUpEmail = findViewById(R.id.sign_up_email);
        signUpMobileNumber = findViewById(R.id.sign_up_mobile_no);
        signUpPassword = findViewById(R.id.sign_up_password);
        signUpCountryCode = findViewById(R.id.sign_up_country_code);
        signUpDob = findViewById(R.id.sign_up_dob);
        signUpCountry = findViewById(R.id.sign_up_country);
        signUpState = findViewById(R.id.sign_up_state);
        signUpCity = findViewById(R.id.sign_up_city);

        signUpText = findViewById(R.id.sign_up_text);


        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermissions();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoggedInActivity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(signUpImage, "home_image");
                pairs[1] = new Pair<View, String>(signUpText, "home_text");
                pairs[2] = new Pair<View, String>(signUpSlogan, "login_desc");
                pairs[3] = new Pair<View, String>(signUpMobileNumber, "login_mobile_number");
                pairs[4] = new Pair<View, String>(signUpPassword, "login_password");
                pairs[5] = new Pair<View, String>(signUp, "login_go_button");
                pairs[6] = new Pair<View, String>(login, "login_sign_up");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
    }

    private void checkAndRequestForPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Please accept required permissions", Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                CropImage.startPickImageActivity(SignUpActivity.this);
            }
        } else {
            CropImage.startPickImageActivity(SignUpActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            pickedImageUrl = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, pickedImageUrl)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                startCrop(pickedImageUrl);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                assert result != null;
                imgUserPhoto.setImageURI(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception exception = result.getError();
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrop(Uri pickedImageUrl) {

        CropImage.activity(pickedImageUrl)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(SignUpActivity.this);
    }

    private boolean validateFirstName() {

        String firstName = Objects.requireNonNull(signUpFirstName.getText()).toString();
        if (firstName.isEmpty()) {
            signUpFirstName.setError("Field cannot be empty");
            return false;
        } else {
            signUpFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {

        String lastName = Objects.requireNonNull(signUpLastName.getText()).toString();
        if (lastName.isEmpty()) {
            signUpLastName.setError("Field cannot be empty");
            return false;
        } else {
            signUpLastName.setError(null);
            return true;
        }
    }

    private boolean validateDob() {
        String dob = Objects.requireNonNull(signUpDob.getText()).toString();

        if (dob.isEmpty()) {
            signUpDob.setError("Field cannot be empty");
            return false;
        } else {
            signUpDob.setError(null);
            return true;
        }
    }

    private boolean validateCountry() {
        String country = Objects.requireNonNull(signUpCountry.getText()).toString();

        if (country.isEmpty()) {
            signUpCountry.setError("Field cannot be empty");
            return false;
        } else {
            signUpCountry.setError(null);
            return true;
        }
    }

    private boolean validateState() {
        String state = Objects.requireNonNull(signUpState.getText()).toString();

        if (state.isEmpty()) {
            signUpState.setError("Field cannot be empty");
            return false;
        } else {
            signUpState.setError(null);
            return true;
        }
    }

    private boolean validateCity() {
        String city = Objects.requireNonNull(signUpCity.getText()).toString();

        if (city.isEmpty()) {
            signUpCity.setError("Field cannot be empty");
            return false;
        } else {
            signUpCity.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {

        String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            signUpEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(pattern)) {
            signUpEmail.setError("Invalid email address");
            return false;
        } else {
            signUpEmail.setError(null);
            return true;
        }
    }

    private boolean validateMobileNo() {

        String mobileNo = Objects.requireNonNull(signUpMobileNumber.getText()).toString();
        String countryCode = Objects.requireNonNull(signUpCountryCode.getText()).toString();

        if (mobileNo.isEmpty()) {
            signUpMobileNumber.setError("Field cannot be empty");
            return false;
        } else if (countryCode.isEmpty()) {
            signUpCountryCode.setError("Field cannot be empty");
            return false;
        } else {
            signUpMobileNumber.setError(null);
            signUpCountryCode.setError(null);

            if (countryCode.charAt(0) == '+') {

                mobileNumberWithCountryCode = countryCode + mobileNo;
            } else {
                mobileNumberWithCountryCode = "+" + countryCode + mobileNo;
            }

            return true;
        }

    }

    private boolean validatePassword() {

        String password = Objects.requireNonNull(signUpPassword.getText()).toString();

        if (password.isEmpty()) {
            signUpPassword.setError("Field cannot be empty");
            return false;
        } else {
            signUpPassword.setError(null);
            return true;
        }
    }

    private boolean validateImage() {
        if (pickedImageUrl == null) {
            Toast.makeText(this, "Select a profile picture", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    //Save data in firebase
    public void registerUser(View view) {

        progressDialog.show();


        //show error
        if (!validateFirstName() | !validateLastName() | !validateEmail() | !validateMobileNo() | !validatePassword() | !validateDob() | !validateCountry() | !validateState() | !validateCity()) {

            progressDialog.dismiss();
            return;
        }


        final String fullName = Objects.requireNonNull(signUpFirstName.getText()).toString() + " " + Objects.requireNonNull(signUpLastName.getText()).toString();
        final String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        String password = Objects.requireNonNull(signUpPassword.getText()).toString();
        final String date_of_birth = signUpDob.getText().toString();
        final String country = Objects.requireNonNull(signUpCountry.getText()).toString();
        final String state = Objects.requireNonNull(signUpState.getText()).toString();
        final String city = Objects.requireNonNull(signUpCity.getText()).toString();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            final DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            db_ref.setValue(true);

                            if (validateImage()) {

                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
                                final StorageReference imageFilePath = mStorage.child(userId + ".jpg");
                                imageFilePath.putFile(pickedImageUrl)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                                imageFilePath.getDownloadUrl()
                                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(final Uri uri) {

                                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                                        .setDisplayName(fullName)
                                                                        .setPhotoUri(uri)
                                                                        .build();

                                                                mAuth.getCurrentUser().updateProfile(profileUpdate);
                                                                db_ref.child("image").setValue(uri.toString());
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }


                            if (!validateImage()) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName)
                                        .build();

                                Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdate);
                            }

                            String device_token = FirebaseInstanceId.getInstance().getToken();

                            db_ref.child("full_name").setValue(fullName);
                            db_ref.child("email").setValue(email);
                            db_ref.child("mobile_no").setValue(mobileNumberWithCountryCode);
                            db_ref.child("device_token").setValue(device_token);
                            db_ref.child("profile_status").setValue("Available");
                            db_ref.child("date_of_birth").setValue(date_of_birth);
                            db_ref.child("country").setValue(country);
                            db_ref.child("state").setValue(state);
                            db_ref.child("city").setValue(city);
                            db_ref.child("books").setValue("no");
                            db_ref.child("updated").setValue("true");
                            db_ref.child("account_created_time").setValue(ServerValue.TIMESTAMP);
                            db_ref.child("settings").child("mobile_number").setValue("false");
                            db_ref.child("settings").child("dob").setValue("false");
                            db_ref.child("settings").child("profile_status").setValue("true");


                            signUpFirstName.setText("");
                            signUpLastName.setText("");
                            signUpEmail.setText("");
                            signUpCountryCode.setText("");
                            signUpMobileNumber.setText("");
                            signUpCountry.setText("");
                            signUpState.setText("");
                            signUpCity.setText("");
                            signUpDob.setText("");
                            signUpPassword.setText("");
                            signUpImage.setImageURI(null);

                            progressDialog.dismiss();

                            //create a Builder object
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);

                            //set builder title
                            builder.setTitle("Registered Successfully");

                            //set builder icon
                            builder.setIcon(R.drawable.success);

                            //set message
                            builder.setMessage("You have to verify your email address, and login with your credentials");

                            //set Button
                            builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //set progress bar visible
                                    progressDialog.show();

                                    //send verification email
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            //email send
                                            if (task.isSuccessful()) {

                                                //set progress bar gone
                                                progressDialog.dismiss();

                                                //show message
                                                Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(SignUpActivity.this, LoggedInActivity.class));

                                                finish();


                                            }
                                            //email not sent
                                            else {

                                                //set progress bar gone
                                                progressDialog.dismiss();

                                                //show message
                                                Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        ;
                                    });
                                }
                            });

                            progressDialog.dismiss();

                            AlertDialog dialog = builder.create();
                            dialog.show();


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //this method shuts application when back is pressed
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

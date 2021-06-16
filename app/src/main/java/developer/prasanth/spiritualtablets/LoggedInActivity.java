package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class LoggedInActivity extends AppCompatActivity {

    Button verifyOTP;
    Button getOTP;
    ImageView image;
    TextView logoText;
    TextView slogan;
    private CountryCodePicker countryCodePicker;
    TextInputLayout otpParent;
    TextInputEditText mobileNumber;
    TextInputEditText otp;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private final static int RC_SIGN_IN = 123;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
    }

    private void init() {

        progressBar = findViewById(R.id.log_in_progress_bar);
        progressDialog = new ProgressDialog(LoggedInActivity.this);
        progressDialog.setCancelable(false);

        image = findViewById(R.id.logoImage);
        verifyOTP = findViewById(R.id.verify_otp);
        getOTP = findViewById(R.id.get_otp);
        logoText = findViewById(R.id.welcome_text);
        slogan = findViewById(R.id.slogan_name);
        countryCodePicker = findViewById(R.id.country_code_picker);

        mobileNumber = findViewById(R.id.login_mobile);
        otp = findViewById(R.id.otp);
        otpParent = findViewById(R.id.otp_parent);


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if ((user != null)) {
                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                }
            }
        };

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredentials(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                showMessage(e.getMessage());
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                super.onCodeSent(s, forceResendingToken);
                showMessage("OTP Sent to your mobile");
                mobileNumber.setFocusable(false);
                progressDialog.dismiss();
                verifyOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage("Please wait while we are verifying the OTP");
                        progressDialog.show();
                        signInWithPhoneAuthCredentials(PhoneAuthProvider.getCredential(s, Objects.requireNonNull(otp.getText()).toString()));
                    }
                });
                otpParent.setVisibility(View.VISIBLE);
                getOTP.setVisibility(View.GONE);
                verifyOTP.setVisibility(View.VISIBLE);
            }
        };
    }

    public void getOtp(View view) {

        String phoneNo = Objects.requireNonNull(mobileNumber.getText()).toString();
        if (!phoneNo.isEmpty()) {

            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait while we are sending OTP to your mobile");
            progressDialog.show();
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber("+" + countryCodePicker.getFullNumber() + phoneNo)
                            .setTimeout(120L, TimeUnit.SECONDS)
                            .setActivity(LoggedInActivity.this)
                            .setCallbacks(callbacks)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else
            showMessage("Please Enter Mobile Number");
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String current_user_id = Objects.requireNonNull(user).getUid();
                            String full_mobile_number = "+" + countryCodePicker.getFullNumberWithPlus() + Objects.requireNonNull(mobileNumber.getText()).toString();
                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id);
                            db_ref.child("mobile_no").setValue(full_mobile_number);
                            progressDialog.dismiss();
                            startActivity(new Intent(LoggedInActivity.this, DashBoardActivity.class));
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
                    }
                });
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (checkInternetConnection())

            firebaseAuth.addAuthStateListener(firebaseAuthListener);
        else

            startActivity(new Intent(LoggedInActivity.this, NoInternetActivity.class));
    }

    private boolean checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        if (connectivityManager != null) {

            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        if ((mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected()))
            return true;

        progressBar.setVisibility(View.GONE);
        showMessage("No Internet Connection");
        return false;
    }

    public void googleSignIn(View view) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoggedInActivity.this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                showMessage(e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        progressDialog.setTitle("Logging you in");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String current_user_id = Objects.requireNonNull(user).getUid();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(account.getDisplayName())
                                    .setPhotoUri(account.getPhotoUrl())
                                    .build();
                            user.updateProfile(profileUpdate);

                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id);
                            db_ref.child("full_name").setValue(account.getDisplayName());
                            db_ref.child("email").setValue(account.getEmail());
                            progressDialog.dismiss();
                            startActivity(new Intent(LoggedInActivity.this, DashBoardActivity.class));
                        } else {

                            showMessage("Unable To Login");
                            progressDialog.dismiss();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
    }

    private void showMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedInActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }
}

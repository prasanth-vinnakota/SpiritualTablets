package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.R;

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
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoggedInActivity extends AppCompatActivity {

    Button verify_otp,get_otp;
    ImageView image;
    TextView logoText, slogan;
    private CountryCodePicker countryCodePicker;
    TextInputLayout otp_parent;
    TextInputEditText mobile_no, otp;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private final static int RC_SIGN_IN = 123;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mProgressBar = findViewById(R.id.log_in_progress_bar);
        progressDialog = new ProgressDialog(LoggedInActivity.this);

        image = findViewById(R.id.logoImage);
        verify_otp = findViewById(R.id.verify_otp);
        get_otp = findViewById(R.id.get_otp);
        logoText = findViewById(R.id.welcome_text);
        slogan = findViewById(R.id.slogan_name);
        countryCodePicker = findViewById(R.id.country_code_picker);

        mobile_no = findViewById(R.id.login_mobile);
        otp = findViewById(R.id.otp);
        otp_parent = findViewById(R.id.otp_parent);


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if ((user != null)) {
                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                }
            }
        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredentials(phoneAuthCredential);
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(LoggedInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(@NonNull final String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(LoggedInActivity.this, "OTP Sent to your mobile", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                verify_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.setMessage("Please wait while we are verifying the OTP");
                        signInWithPhoneAuthCredentials(PhoneAuthProvider.getCredential(s, Objects.requireNonNull(otp.getText()).toString()));
                    }
                });
                otp_parent.setVisibility(View.VISIBLE);
                get_otp.setVisibility(View.GONE);
                verify_otp.setVisibility(View.VISIBLE);

            }
        };
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String current_user_id = Objects.requireNonNull(user).getUid();
                            String full_mobile_number = "+"+countryCodePicker.getFullNumberWithPlus()+ Objects.requireNonNull(mobile_no.getText()).toString();
                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id);
                            db_ref.child("mobile_no").setValue(full_mobile_number);
                            db_ref.child("device_token").setValue(FirebaseInstanceId.getInstance().getToken());
                            db_ref.child("profile_status").setValue("Available");
                            progressDialog.dismiss();
                            startActivity(new Intent(LoggedInActivity.this, DashBoardActivity.class));
                        } else {
                            Toast.makeText(LoggedInActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
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
        if ((mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected())) {

            //exit
            return true;
        }

        //hide progress bar
        mProgressBar.setVisibility(View.GONE);

        //show toast message
        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //add firebaseAuthStateListener
        if (checkInternetConnection())
            mAuth.addAuthStateListener(firebaseAuthListener);
        else
            startActivity(new Intent(LoggedInActivity.this, NoInternetActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        //remove firebaseAuthStateListener
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    //this method shuts application when back is pressed
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        progressDialog.setTitle("Logging you in");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String current_user_id = Objects.requireNonNull(user).getUid();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(account.getDisplayName())
                                    .setPhotoUri(account.getPhotoUrl())
                                    .build();
                            user.updateProfile(profileUpdate);

                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id);
                            db_ref.child("full_name").setValue(account.getDisplayName());
                            db_ref.child("email").setValue(account.getEmail());
                            db_ref.child("device_token").setValue(FirebaseInstanceId.getInstance().getToken());
                            db_ref.child("profile_status").setValue("Available");
                            progressDialog.dismiss();
                            startActivity(new Intent(LoggedInActivity.this, DashBoardActivity.class));
                        } else {
                            Toast.makeText(LoggedInActivity.this, "Unable to Login please try to login normally", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void getOtp(View view) {
        String phoneNo = Objects.requireNonNull(mobile_no.getText()).toString();
        if (!phoneNo.isEmpty()) {
            progressDialog.setTitle("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait while we are sending OTP to your mobile");
            progressDialog.show();
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                            .setPhoneNumber("+"+countryCodePicker.getFullNumber() + phoneNo)
                            .setTimeout(120L, TimeUnit.SECONDS)
                            .setActivity(LoggedInActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            Toast.makeText(LoggedInActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        }
    }
}

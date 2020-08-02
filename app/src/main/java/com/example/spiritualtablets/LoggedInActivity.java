package com.example.spiritualtablets;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoggedInActivity extends AppCompatActivity {

    Button newUser, logIn, forgetPassword;
    ImageView image;
    TextView logoText, slogan;
    TextInputEditText username, password;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    private final static int RC_SIGN_IN = 123;
    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        coordinatorLayout = findViewById(R.id.login_activity);

        newUser = findViewById(R.id.new_user);
        logIn = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forget_password);

        mProgressBar = findViewById(R.id.log_in_progress_bar);
        progressDialog = new ProgressDialog(LoggedInActivity.this);
        progressDialog.setMessage("please wait while we are logging you in");
        progressDialog.setTitle("Logging in");

        image = findViewById(R.id.logoImage);

        logoText = findViewById(R.id.welcome_text);
        slogan = findViewById(R.id.slogan_name);

        username = findViewById(R.id.login_email);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if ((user != null)) {

                    if (user.isEmailVerified()) {
                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                    } else {

                        //create a Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedInActivity.this);

                        //set title to builder
                        builder.setTitle("Email is not verified");

                        //set icon
                        builder.setIcon(R.drawable.danger);

                        //set message
                        builder.setMessage("Your e-mail address " + user.getEmail() + " is not verified, please verify your email address and login again");

                        //set Button
                        builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //set progress bar visible
                                mProgressBar.setVisibility(View.VISIBLE);

                                //send verification email
                                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(LoggedInActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //email send
                                        if (task.isSuccessful()) {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();

                                        }
                                        //email not sent
                                        else {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                        mProgressBar.setVisibility(View.GONE);

                        //build and show builder
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            }
        };

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInternetConnection())
                    startActivity(new Intent(LoggedInActivity.this, NoInternetActivity.class));

                Intent intent = new Intent(LoggedInActivity.this, SignUpActivity.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(image, "home_image");
                pairs[1] = new Pair<View, String>(logoText, "home_text");
                pairs[2] = new Pair<View, String>(slogan, "login_desc");
                pairs[3] = new Pair<View, String>(username, "login_username");
                pairs[4] = new Pair<View, String>(password, "login_password");
                pairs[5] = new Pair<View, String>(logIn, "login_go_button");
                pairs[6] = new Pair<View, String>(newUser, "login_sign_up");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoggedInActivity.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInternetConnection())
                    startActivity(new Intent(LoggedInActivity.this, NoInternetActivity.class));

                String email = Objects.requireNonNull(username.getText()).toString();

                if (validateUsername())
                    return;

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(LoggedInActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            //set progress bar gone
                            mProgressBar.setVisibility(View.GONE);

                            //show message
                            Toast.makeText(getApplicationContext(), "Password reset email sent to your email", Toast.LENGTH_SHORT).show();
                        }
                        //email not send
                        else {

                            //set progress bar gone
                            mProgressBar.setVisibility(View.GONE);

                            //show message
                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private boolean validateUsername() {

        String userName = Objects.requireNonNull(username.getText()).toString();
        if (userName.isEmpty()) {
            username.setError("Field cannot be empty");
            return true;
        } else {
            username.setError(null);
            return false;
        }
    }

    private boolean validatePassword() {

        String passWord = Objects.requireNonNull(password.getText()).toString();

        if (passWord.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {

        if (!checkInternetConnection())
            startActivity(new Intent(LoggedInActivity.this, NoInternetActivity.class));

        if (validateUsername() | !validatePassword())
            return;

        //mProgressBar.setVisibility(View.VISIBLE);
        progressDialog.show();

        String userName = Objects.requireNonNull(username.getText()).toString();
        String passWord = Objects.requireNonNull(password.getText()).toString();

        mAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(LoggedInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {

                        mProgressBar.setVisibility(View.GONE);

                        String device_token = FirebaseInstanceId.getInstance().getToken();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("device_token");

                        userRef.setValue(device_token)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(LoggedInActivity.this, DashBoardActivity.class);
                                            Snackbar.make(coordinatorLayout, "Logging in", Snackbar.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {

                        //create a Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedInActivity.this);

                        //set title to builder
                        builder.setTitle("Email is not verified");

                        //set icon
                        builder.setIcon(R.drawable.danger);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        //set message
                        assert user != null;
                        builder.setMessage("Your e-mail address " + user.getEmail() + " is not verified, please verify your email address and login again");

                        //set Button
                        builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //set progress bar visible
                                mProgressBar.setVisibility(View.VISIBLE);

                                //send verification email
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(LoggedInActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //email send
                                        if (task.isSuccessful()) {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Snackbar.make(coordinatorLayout, "Verification email sent", Snackbar.LENGTH_SHORT).show();

                                        }
                                        //email not sent
                                        else {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Snackbar.make(coordinatorLayout, Objects.requireNonNull(task.getException()).toString(), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        progressDialog.dismiss();
                        //mProgressBar.setVisibility(View.GONE);

                        //build and show builder
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                } else {

                    //set progress bar gone
                    mProgressBar.setVisibility(View.GONE);

                    //show message
                    Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_SHORT).show();

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

}

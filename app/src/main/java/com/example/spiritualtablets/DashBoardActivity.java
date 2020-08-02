package com.example.spiritualtablets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void openBooks(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);

        builder.setTitle("Select language");

        builder.setIcon(R.drawable.book);

        builder.setPositiveButton("Telugu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(DashBoard.this, "Please wait books are loading...",Toast.LENGTH_LONG).show();
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this, TeluguBookListActivity.class));
                loadingDialog.dismiss();
            }
        });

        builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(DashBoard.this, "Please wait books are loading...",Toast.LENGTH_LONG).show();
                LoadingDialog loadingDialog = new LoadingDialog(DashBoardActivity.this);
                loadingDialog.startLoading();
                startActivity(new Intent(DashBoardActivity.this, EnglishBookListActivity.class));
                loadingDialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setData(Uri.parse("https://www.payumoney.com/react/app/merchant/#/pay/merchant/A381DF3EC1177559CC7B5B2440F3DC67?param=7102505"));
       startActivity(intent);
    }

    public void contactUs(View view) {

        startActivity(new Intent(this, ContactUsActivity.class));
    }

    public void dailyTip(View view) {

        startActivity(new Intent(this, DailyTipActivity.class));
    }

    public void chat (View view){

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
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://spiritualtablet.org/RequestForCounseling.html")));
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

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoggedInActivity.class));
        finish();
    }

    public void settings(View view) {

        startActivity(new Intent(DashBoardActivity.this, SettingsActivity.class));
    }
}

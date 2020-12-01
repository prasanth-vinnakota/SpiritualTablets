package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import developer.prasanth.spiritualtablets.contact_us.PrimaryCentersActivity;

public class ContactUsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(ContactUsActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(ContactUsActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void registeredOffice(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Registered Office");
        builder.setMessage("SPIRITUAL TABLETS RESEARCH FOUNDATION,\n" +
                "VUDA COLONY,\n" +
                "48 A LAST BUS STOP,\n" +
                "MADHAVADHARA,\n" +
                "VISAKHAPATNAM,\n" +
                "PIN CODE - 53007\n" +
                "PH NO : 9246648401/402\n" +
                "MAIL TO : spiritualtablet@gmail.com");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void primaryCenters(View view) {

        startActivity(new Intent(this, PrimaryCentersActivity.class));
    }

    public void admissionCenters(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Admission Centers");
        builder.setMessage("1.NITYANADHA PYRAMID\n" +
                "2.JAGANNATH PYRAMID\n" +
                "3.MAADUGULA PYRAMID\n" +
                "4.GUDIWADA PYRAMID\n" +
                "5.MUMMIDIVARAM PYRAMID\n" +
                "6.PEDAGADI PYRAMID\n" +
                "7.KOTALA PYRAMID" );

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void holisticCenters(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactUsActivity.this);

        builder.setTitle("Holistic Centers");
        builder.setMessage("Pyramid Valley,\n Bengaluru\n" +
                "www.pyamidvalley.org\n" +
                "\n" +
                "\n" +
                "Kadtal Maheswara Maha Pyramid\n" +
                "www.maheshwarapyramid.org");

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void developer(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactUsActivity.this);
        builder.setTitle("Contact App Developer");
        builder.setMessage("Name : Srinivas Prasanth Vinnakota\nEmail : prasanth_vinnakota@yahoo.com\nMobile no : +919100362607");
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                copyOrCallAlertDialog("+919100362607");
            }
        });

        builder.setNegativeButton("Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:prasanth_vinnakota@yahoo.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT,"Contact Developer");
                startActivity(intent);
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void copyOrCallAlertDialog(final String message){

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(ContactUsActivity.this).inflate(R.layout.copy_or_call_dialog,null);

        final Button call = view.findViewById(R.id.copy_or_call_dialog_call);
        final Button copy = view.findViewById(R.id.copy_or_call_dialog_copy);

        final AlertDialog alertDialog = new AlertDialog.Builder(ContactUsActivity.this)
                .setCancelable(true)
                .setView(view)
                .create();

        alertDialog.show();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(message);
                alertDialog.dismiss();
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(message);
                alertDialog.dismiss();
            }
        });

    }

    private void call(String number){

        if (ContextCompat.checkSelfPermission(ContactUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactUsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
        }else {
            String dial = "tel:" +number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            finish();
        }
    }

    private void copy(String message){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("number", message);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
            showMessage("Number copied to clipboard");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                showMessage("Permissions Granted Now You Can Make Calls");
        }
    }

    public void counsellingEnglishAndHindiNumber(View view) {

        copyOrCallAlertDialog("+917899801922");
    }

    public void counsellingTeluguNumber(View view) {


        copyOrCallAlertDialog("+916303465603");
    }

    public void workshopNumber(View view) {

        copyOrCallAlertDialog("+918885352809");
    }

    public void workshopSecondNumber(View view) {

        copyOrCallAlertDialog("+918333052547");
    }

    public void anandobrahmaNumber(View view) {

        copyOrCallAlertDialog("+919246648405");
    }

    public void europeSessionsNumber(View view) {

        copyOrCallAlertDialog("+919246648405");
    }

    public void donationNumber(View view) {

        copyOrCallAlertDialog("+919553801801");
    }

    public void spiritualParentingNumber(View view) {

        copyOrCallAlertDialog("+918008117037");
    }

    public void personalAppointmentNumber(View view) {

        copyOrCallAlertDialog("+919550093952");
    }

    public void pmcUKYoutubeChannelNumber(View view) {

        copyOrCallAlertDialog("+447440604222");
    }

    private void showMessage(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactUsActivity.this);
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.create().show();
    }
}

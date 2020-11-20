package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.contact_us.PrimaryCentersActivity;

public class ContactUsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void registeredOffice(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Registered Office");
        builder.setMessage("SPIRITUAL TABLETS RESEARCH FOUNDATION" +
                "LIG. -6" +
                "VUDA COLONY LAST BUT STOP" +
                "MADHAVADHARA" +
                "VISAKHAPATNAM" +
                "PH NO : 9246648401/402");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void primaryCenters(View view) {

        startActivity(new Intent(this, PrimaryCentersActivity.class));
    }

    public void admissionCenters(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Admission Centers");
        builder.setMessage("1.REVALLAPALAM" +
                "2.JAGANNATH PYRAMID" +
                "3.MAADUGULA" +
                "4.GUDIWADA" +
                "5.MUMMIDIVARAM" +
                "6.PEDAGADI PYRAMID" +
                "7.KOTALA PYRAMID");

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void holisticCenters(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactUsActivity.this);

        builder.setTitle("Holistic Centers");
        builder.setMessage("Pyramid Valley, Bengaluru\n" +
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
                call();
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

    private void call(){
        if (ContextCompat.checkSelfPermission(ContactUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ContactUsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
        }else {
            String dial = "tel:" +"+919100362607";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                call();
            }else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

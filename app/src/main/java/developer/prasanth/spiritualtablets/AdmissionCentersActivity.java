package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdmissionCentersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_centers);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdmissionCentersActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdmissionCentersActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void nithyanandaPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Nithyanandha Pyramid");
        startActivity(intent);
    }

    public void jagannathPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Jagannath Pyramid");
        startActivity(intent);
    }

    public void maadugulaPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Maadugula Pyramid");
        startActivity(intent);
    }

    public void gudiwadaPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Gudiwada Pyramid");
        startActivity(intent);
    }

    public void mummidivaramPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Mummuduvaram Pyramid");
        startActivity(intent);
    }

    public void pedagadiPyramid(View view) {
        Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Padagadi Pyramid");
        startActivity(intent);
    }

    public void kotalaPyramid(View view) { Intent intent = new Intent(AdmissionCentersActivity.this, AdmissionCentersBriefActivity.class);
        intent.putExtra("name","Kotala Pyramid");
        startActivity(intent);
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}
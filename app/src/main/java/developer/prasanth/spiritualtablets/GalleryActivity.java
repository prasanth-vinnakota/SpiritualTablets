package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import developer.prasanth.spiritualtablets.adapters.GalleryAdapter;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(GalleryActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(GalleryActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        init();

        databaseReference = FirebaseDatabase.getInstance().getReference("gallery");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (snapshot.exists()){

                     List<String> list = new ArrayList<>();
                     for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                         list.add(dataSnapshot.getKey());
                     }

                     galleryAdapter = new GalleryAdapter(GalleryActivity.this, list);
                     recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));
                     recyclerView.setAdapter(galleryAdapter);
                     progressDialog.dismiss();
                 }
                 else {
                     progressDialog.dismiss();
                     showMessage();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void init(){

        recyclerView = findViewById(R.id.galleryRV);
        progressDialog = new ProgressDialog(GalleryActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait while gallery is loading");
        progressDialog.show();
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    private void showMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setMessage("No Data Available");
        builder.setCancelable(true);
        builder.create().show();
    }
}

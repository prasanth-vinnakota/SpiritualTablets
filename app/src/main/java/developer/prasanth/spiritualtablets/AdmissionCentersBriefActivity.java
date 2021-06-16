package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import developer.prasanth.spiritualtablets.adapters.AdmissionCentersImageAdapter;
import developer.prasanth.spiritualtablets.adapters.ReviewAdapter;
import developer.prasanth.spiritualtablets.models.ItemBean;
import developer.prasanth.spiritualtablets.models.ReviewBean;

public class AdmissionCentersBriefActivity extends AppCompatActivity {

    List<ItemBean> itemBeans;
    List<ReviewBean> reviewBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_centers_brief);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdmissionCentersBriefActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AdmissionCentersBriefActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String name = getIntent().getStringExtra("name");
        TextView title = findViewById(R.id.activity_admission_center_title_name);
        title.setText(name);

        TextView name_of_contact_person = findViewById(R.id.activity_admission_center_contact_person_name);
        TextView number_of_contact_person = findViewById(R.id.activity_admission_center_contact_person_number);
        TextView address = findViewById(R.id.activity_admission_center_contact_pyramid_address);
        RatingBar rating_bar = findViewById(R.id.activity_admission_center_rating_bar);

        RecyclerView imagesRecyclerView = findViewById(R.id.activity_admission_center_images_RV);
        RecyclerView reviewRecyclerView = findViewById(R.id.activity_admission_center_review_RV);

        DatabaseReference admission_center_ref = FirebaseDatabase.getInstance().getReference("Admission Centers").child(name);
        admission_center_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check)
                    if (snapshot.exists()) {

                        if (snapshot.child("Contact Details").getValue() != null) {
                            if (snapshot.child("Contact Details").child("name").getValue() != null)
                                name_of_contact_person.setText(snapshot.child("Contact Details").child("name").getValue().toString());
                            if (snapshot.child("Contact Details").child("mobile_no").getValue() != null)
                                number_of_contact_person.setText(snapshot.child("Contact Details").child("mobile_no").getValue().toString());
                            if (snapshot.child("Contact Details").child("address").getValue() != null)
                                address.setText(snapshot.child("Contact Details").child("address").getValue().toString());
                        }
                    } else {
                        Toast.makeText(AdmissionCentersBriefActivity.this, "No data available about " + name, Toast.LENGTH_SHORT).show();
                    }
                check = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference images_ref = FirebaseDatabase.getInstance().getReference("Admission Centers").child(name).child("Images");
        images_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check)
                    if (snapshot.exists()) {
                        itemBeans = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ItemBean itemBean = new ItemBean();
                            itemBean.setName(dataSnapshot.child("name").getValue().toString());
                            itemBean.setLink(dataSnapshot.child("link").getValue().toString());
                            //Toast.makeText(AdmissionCentersBriefActivity.this, itemBean.getLink(), Toast.LENGTH_SHORT).show();
                            itemBeans.add(itemBean);
                        }

                        imagesRecyclerView.setLayoutManager(new GridLayoutManager(AdmissionCentersBriefActivity.this, 3));
                        AdmissionCentersImageAdapter admissionCentersImageAdapter = new AdmissionCentersImageAdapter(AdmissionCentersBriefActivity.this, itemBeans);
                        imagesRecyclerView.setAdapter(admissionCentersImageAdapter);

                    }
                check = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference review_ref = FirebaseDatabase.getInstance().getReference("Admission Centers").child(name).child("Review");
        review_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (check)
                    if (snapshot.exists()){
                        int count = (int) snapshot.getChildrenCount();
                        int sum = 0;
                        reviewBeans = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ReviewBean reviewBean = new ReviewBean();
                            reviewBean.setComments(dataSnapshot.child("comment").getValue().toString());
                            reviewBean.setName(dataSnapshot.child("name").getValue().toString());
                            reviewBean.setStars(dataSnapshot.child("stars").getValue().toString());
                            reviewBeans.add(reviewBean);
                            sum += Integer.parseInt(reviewBean.getStars());
                        }
                        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(AdmissionCentersBriefActivity.this));
                        ReviewAdapter reviewAdapter = new ReviewAdapter(AdmissionCentersBriefActivity.this, reviewBeans);
                        reviewRecyclerView.setAdapter(reviewAdapter);
                        float rating =(float) sum/count;
                        rating_bar.setNumStars(5);
                        rating_bar.setRating(rating);
                    }

                check = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}
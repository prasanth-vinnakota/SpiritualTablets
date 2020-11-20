package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import developer.prasanth.spiritualtablets.R;
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

        recyclerView = findViewById(R.id.galleryRV);
        progressDialog = new ProgressDialog(GalleryActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait while gallery is loading");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("gallery");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                     List<String> list = new ArrayList<>();
                     for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                         list.add(dataSnapshot.getValue(String.class));
                     }

                     galleryAdapter = new GalleryAdapter(GalleryActivity.this, list);
                     recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));
                     recyclerView.setAdapter(galleryAdapter);
                     progressDialog.dismiss();
                 }
                 else {
                     progressDialog.dismiss();
                     Toast.makeText(GalleryActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

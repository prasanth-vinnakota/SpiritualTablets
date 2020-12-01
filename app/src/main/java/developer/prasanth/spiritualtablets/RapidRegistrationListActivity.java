package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import developer.prasanth.spiritualtablets.adapters.RapidRegistrationAdapter;

public class RapidRegistrationListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> arrayList;
    LoadingDialog loadingDialog;
    RapidRegistrationAdapter rapidRegistrationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_registration_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RapidRegistrationListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RapidRegistrationListActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        recyclerView = findViewById(R.id.rapid_registrationRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(RapidRegistrationListActivity.this));
        arrayList = new ArrayList<>();
        loadingDialog = new LoadingDialog(RapidRegistrationListActivity.this);

        switch (Objects.requireNonNull(getIntent().getStringExtra("type"))){

            case "checked":
                loadingDialog.startLoading();
                DatabaseReference checked_ref = FirebaseDatabase.getInstance().getReference("checked_rapid_registration");
                checked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot :snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            rapidRegistrationAdapter = new RapidRegistrationAdapter(arrayList,RapidRegistrationListActivity.this);
                            recyclerView.setAdapter(rapidRegistrationAdapter);
                        }
                        else {
                            showMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                loadingDialog.dismiss();
                break;

            case "unchecked":
                loadingDialog.startLoading();
                DatabaseReference unchecked_ref = FirebaseDatabase.getInstance().getReference("unchecked_rapid_registration");
                unchecked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot :snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            rapidRegistrationAdapter = new RapidRegistrationAdapter(arrayList,RapidRegistrationListActivity.this);
                            recyclerView.setAdapter(rapidRegistrationAdapter);
                        }
                        else {
                            showMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                loadingDialog.dismiss();
                break;

            case "all":
                loadingDialog.startLoading();
                DatabaseReference all_ref = FirebaseDatabase.getInstance().getReference("rapid_registration");
                all_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot :snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            rapidRegistrationAdapter = new RapidRegistrationAdapter(arrayList,RapidRegistrationListActivity.this);
                            recyclerView.setAdapter(rapidRegistrationAdapter);
                        }
                        else {
                            showMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                loadingDialog.dismiss();
                break;
        }
    }
    private void showMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RapidRegistrationListActivity.this);
        builder.setMessage("No Data Available");
        builder.setCancelable(true);
        builder.create().show();
    }
}
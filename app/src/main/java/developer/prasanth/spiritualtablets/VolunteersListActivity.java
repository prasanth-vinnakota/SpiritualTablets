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

import developer.prasanth.spiritualtablets.adapters.VolunteerAdapter;

public class VolunteersListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VolunteerAdapter volunteerAdapter;
    List<String> stringList;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteers_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VolunteersListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(VolunteersListActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        init();

        switch (Objects.requireNonNull(getIntent().getStringExtra("type"))){

            case "checked":

                loadingDialog.startLoading();
                DatabaseReference checkedReference = FirebaseDatabase.getInstance().getReference("checked_volunteer");
                checkedReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                stringList.add(dataSnapshot.getKey());
                            }
                            volunteerAdapter = new VolunteerAdapter(stringList,VolunteersListActivity.this);
                            recyclerView.setAdapter(volunteerAdapter);
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
                DatabaseReference uncheckedReference = FirebaseDatabase.getInstance().getReference("unchecked_volunteer");
                uncheckedReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                stringList.add(dataSnapshot.getKey());
                            }
                            volunteerAdapter = new VolunteerAdapter(stringList,VolunteersListActivity.this);
                            recyclerView.setAdapter(volunteerAdapter);
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
                DatabaseReference volunteersReference = FirebaseDatabase.getInstance().getReference("volunteer_registration");
                volunteersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                stringList.add(dataSnapshot.getKey());
                            }
                            volunteerAdapter = new VolunteerAdapter(stringList,VolunteersListActivity.this);
                            recyclerView.setAdapter(volunteerAdapter);
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

    private void init(){

        recyclerView = findViewById(R.id.volunteersRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(VolunteersListActivity.this));
        stringList = new ArrayList<>();
        loadingDialog = new LoadingDialog(VolunteersListActivity.this);
    }

    private void showMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(VolunteersListActivity.this);
        builder.setMessage("No Data Available");
        builder.setCancelable(true);
        builder.create().show();
    }
}
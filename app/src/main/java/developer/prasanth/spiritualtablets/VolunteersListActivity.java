package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import developer.prasanth.spiritualtablets.adapters.VolunteerListAdapter;

public class VolunteersListActivity extends AppCompatActivity {

    RecyclerView volunteerRV;
    VolunteerListAdapter volunteerListAdapter;
    ArrayList<String> volunteerList;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteers_list);
        volunteerRV = findViewById(R.id.volunteersRV);
        volunteerRV.setLayoutManager(new LinearLayoutManager(VolunteersListActivity.this));
        volunteerList = new ArrayList<>();
        loadingDialog = new LoadingDialog(VolunteersListActivity.this);

        switch (Objects.requireNonNull(getIntent().getStringExtra("type"))){
            case "checked":
                loadingDialog.startLoading();
                DatabaseReference checked_ref = FirebaseDatabase.getInstance().getReference("checked_volunteer");
                checked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                volunteerList.add(dataSnapshot.getKey());
                            }
                            volunteerListAdapter = new VolunteerListAdapter(volunteerList,VolunteersListActivity.this);
                            volunteerRV.setAdapter(volunteerListAdapter);
                        }
                        else {
                            Toast.makeText(VolunteersListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
                DatabaseReference unchecked_ref = FirebaseDatabase.getInstance().getReference("unchecked_volunteer");
                unchecked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                volunteerList.add(dataSnapshot.getKey());
                            }
                            volunteerListAdapter = new VolunteerListAdapter(volunteerList,VolunteersListActivity.this);
                            volunteerRV.setAdapter(volunteerListAdapter);
                        }
                        else {
                            Toast.makeText(VolunteersListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
                DatabaseReference volunteers_ref = FirebaseDatabase.getInstance().getReference("volunteer_registration");
                volunteers_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                volunteerList.add(dataSnapshot.getKey());
                            }
                            volunteerListAdapter = new VolunteerListAdapter(volunteerList,VolunteersListActivity.this);
                            volunteerRV.setAdapter(volunteerListAdapter);
                        }
                        else {
                            Toast.makeText(VolunteersListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
}
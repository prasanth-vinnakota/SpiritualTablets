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

import developer.prasanth.spiritualtablets.adapters.RequestForCounsellingAdapter;


public class RequestForCounsellingListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> arrayList;
    LoadingDialog loadingDialog;
    RequestForCounsellingAdapter requestForCounsellingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_counselling_list);

        recyclerView = findViewById(R.id.request_for_counsellingRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(RequestForCounsellingListActivity.this));
        arrayList = new ArrayList<>();
        loadingDialog = new LoadingDialog(RequestForCounsellingListActivity.this);

        switch (Objects.requireNonNull(getIntent().getStringExtra("type"))){
            case "checked":
                loadingDialog.startLoading();
                DatabaseReference checked_ref = FirebaseDatabase.getInstance().getReference("checked_request_for_counselling");
                checked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            requestForCounsellingAdapter = new RequestForCounsellingAdapter(arrayList,RequestForCounsellingListActivity.this);
                            recyclerView.setAdapter(requestForCounsellingAdapter);
                        }
                        else {
                            Toast.makeText(RequestForCounsellingListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
                DatabaseReference unchecked_ref = FirebaseDatabase.getInstance().getReference("unchecked_request_for_counselling");
                unchecked_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            requestForCounsellingAdapter = new RequestForCounsellingAdapter(arrayList,RequestForCounsellingListActivity.this);
                            recyclerView.setAdapter(requestForCounsellingAdapter);
                        }
                        else {
                            Toast.makeText(RequestForCounsellingListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
                DatabaseReference all_ref = FirebaseDatabase.getInstance().getReference("request_for_counselling");
                all_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                arrayList.add(dataSnapshot.getKey());
                            requestForCounsellingAdapter = new RequestForCounsellingAdapter(arrayList,RequestForCounsellingListActivity.this);
                            recyclerView.setAdapter(requestForCounsellingAdapter);
                        }
                        else {
                            Toast.makeText(RequestForCounsellingListActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
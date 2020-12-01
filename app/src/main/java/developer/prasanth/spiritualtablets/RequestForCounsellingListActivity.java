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

import developer.prasanth.spiritualtablets.adapters.RequestForCounsellingAdapter;


public class RequestForCounsellingListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> arrayList;
    LoadingDialog loadingDialog;
    RequestForCounsellingAdapter requestForCounsellingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_counselling_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RequestForCounsellingListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RequestForCounsellingListActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


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

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestForCounsellingListActivity.this);
        builder.setMessage("No Data Available");
        builder.setCancelable(true);
        builder.create().show();
    }
}
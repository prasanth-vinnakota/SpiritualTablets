package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import developer.prasanth.spiritualtablets.adapters.FindFriendsAdapter;
import developer.prasanth.spiritualtablets.models.Contacts;

public class ContactsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference userRef, contactsRef;
    private List<String> contactsList;
    private GetUsersThroughUserId getUsersThroughUserId;;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = findViewById(R.id.contacts_activity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Contacts");


        ProgressDialog progressDialog = new ProgressDialog(ContactsActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait while we are retrieving users information");

        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference("users");
        contactsRef = FirebaseDatabase.getInstance().getReference("contacts").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());


        contactsList = new ArrayList<>();

        recyclerView = findViewById(R.id.contacts_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));

        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                       contactsList.add(dataSnapshot.getKey());
                    }


                    getUsersThroughUserId = new GetUsersThroughUserId(contactsList, ContactsActivity.this);
                    recyclerView.setAdapter(getUsersThroughUserId);
                }else{
                    Toast.makeText(ContactsActivity.this, "Make Friends To Display Them In Contacts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getUsersThroughUserId.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    private void updateUserStatus(String state){

        String saveCurrentTime, saveCurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineState = new HashMap<>();
        onlineState.put("time", saveCurrentTime);
        onlineState.put("date", saveCurrentDate);
        onlineState.put("state", state);


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        rootRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("user_state")
                .updateChildren(onlineState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserStatus("online");
    }

}
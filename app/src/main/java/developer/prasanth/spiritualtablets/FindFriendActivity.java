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

public class FindFriendActivity extends AppCompatActivity {

    private RecyclerView findFriendsRv;
    private DatabaseReference userRef;
    private FindFriendsAdapter findFriendsAdapter;
    private List<Contacts> contactsList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Toolbar toolbar = findViewById(R.id.find_friend_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Find Friends");

        ProgressDialog progressDialog = new ProgressDialog(FindFriendActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait while we are retrieving users information");

        userRef = FirebaseDatabase.getInstance().getReference("users");

        contactsList = new ArrayList<>();

        findFriendsRv = findViewById(R.id.find_friend_recycler_view);
        findFriendsRv.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Contacts contacts = dataSnapshot.getValue(Contacts.class);
                    /*contacts.setFull_name(Objects.requireNonNull(dataSnapshot.child("full_name").getValue()).toString());
                    contacts.setEmail(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                    contacts.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());*/
                    assert contacts != null;
                    contacts.setUserId(dataSnapshot.getKey());
                    contactsList.add(contacts);
                }

                findFriendsAdapter = new FindFriendsAdapter(FindFriendActivity.this, contactsList);
                findFriendsRv.setAdapter(findFriendsAdapter);
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
                findFriendsAdapter.getFilter().filter(s);
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
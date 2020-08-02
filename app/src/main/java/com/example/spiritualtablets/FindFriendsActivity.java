package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spiritualtablets.adapters.FindFriendsAdapter;
import com.example.spiritualtablets.models.Contacts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivity extends AppCompatActivity {

    private RecyclerView findFriendsRv;
    private DatabaseReference userRef;
    private FindFriendsAdapter findFriendsAdapter;
    private List<Contacts> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Toolbar toolbar = findViewById(R.id.find_friend_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ProgressDialog progressDialog = new ProgressDialog(FindFriendsActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait while we are retrieving users information");

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        contactsList = new ArrayList<>();

        findFriendsRv = findViewById(R.id.find_friend_recycler_view);
        findFriendsRv.setLayoutManager(new LinearLayoutManager(FindFriendsActivity.this));

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Contacts contacts = new Contacts();
                    contacts.setFull_name(Objects.requireNonNull(dataSnapshot.child("full_name").getValue()).toString());
                    contacts.setEmail(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                    contacts.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                    contacts.setUserId(dataSnapshot.getKey());
                    contactsList.add(contacts);
                }

                findFriendsAdapter = new FindFriendsAdapter(FindFriendsActivity.this, contactsList);
                findFriendsRv.setAdapter(findFriendsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

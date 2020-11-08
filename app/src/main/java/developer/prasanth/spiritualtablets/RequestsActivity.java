package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import developer.prasanth.spiritualtablets.models.Contacts;

public class RequestsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    private DatabaseReference userRef, contactRef, requestRef;
    private String currentUserId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        toolbar = findViewById(R.id.request_activity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Friend Requests");

        recyclerView = findViewById(R.id.request_list_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        requestRef = FirebaseDatabase.getInstance().getReference("chat_requests");
        contactRef = FirebaseDatabase.getInstance().getReference("contacts");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(requestRef.child(currentUserId),Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, RequestViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {


                final String user_id  = getRef(position).getKey();


                DatabaseReference request_type_ref = getRef(position).child("request_type").getRef();

                request_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            String request_type = Objects.requireNonNull(snapshot.getValue()).toString();

                            if (request_type.equals("received")){

                                holder.accept.setVisibility(View.VISIBLE);
                                holder.reject.setVisibility(View.VISIBLE);

                                userRef = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(user_id));
                                userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                                        holder.userEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                                        holder.userName.setText(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());
                                        if (snapshot.child("image").getValue() != null)
                                            Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(holder.userImage);


                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                CharSequence[] options = new CharSequence[]{ "Accept", "Reject"};

                                                AlertDialog.Builder builder = new AlertDialog.Builder(RequestsActivity.this);
                                                builder.setTitle("Accept or Reject Request");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(final DialogInterface dialog, int which) {

                                                        if (which == 0){

                                                            contactRef.child(currentUserId).child(user_id).child("contact")
                                                                    .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    contactRef.child(user_id).child(currentUserId).child("contact")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            requestRef.child(currentUserId)
                                                                                    .child(user_id)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            requestRef.child(user_id)
                                                                                                    .child(currentUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            Toast.makeText(RequestsActivity.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                                                                                                            dialog.dismiss();
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                                }
                                                            });

                                                        }
                                                        if (which == 1){

                                                            requestRef.child(currentUserId)
                                                                    .child(user_id)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            requestRef.child(user_id)
                                                                                    .child(currentUserId)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Toast.makeText(RequestsActivity.this, "Request Rejected", Toast.LENGTH_SHORT).show();
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });

                                                        }

                                                    }
                                                });

                                                builder.show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else if (request_type.equals("sent")){

                                holder.accept.setText(R.string.cancel_request);
                                holder.accept.setVisibility(View.VISIBLE);

                                userRef = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(user_id));
                                userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){


                                            holder.userEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                                            holder.userName.setText(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());
                                            if (snapshot.child("image").getValue() != null)
                                                Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(holder.userImage);


                                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    CharSequence[] options = new CharSequence[]{ "Cancel Request", "Dismiss"};

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(RequestsActivity.this);
                                                    builder.setTitle("Cancel Request?");

                                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(final DialogInterface dialogInterface, int i) {
                                                            if (i == 0){

                                                                requestRef.child(currentUserId)
                                                                        .child(user_id)
                                                                        .removeValue()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                requestRef.child(user_id)
                                                                                        .child(currentUserId)
                                                                                        .removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                Toast.makeText(RequestsActivity.this, "Request Canceled", Toast.LENGTH_SHORT).show();
                                                                                                dialogInterface.dismiss();
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                            if (i == 1){
                                                                dialogInterface.dismiss();
                                                            }
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(RequestsActivity.this, "No Pending Requests", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(RequestsActivity.this).inflate(R.layout.users_display_layout, parent, false);

                return new RequestViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    static class RequestViewHolder extends  RecyclerView.ViewHolder{


        TextView userName, userEmail;
        CircleImageView userImage;
        ImageView userOnlineStatus;
        Button accept, reject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_display_layout_user_image);
            userOnlineStatus = itemView.findViewById(R.id.user_display_layout_user_online);

            userName = itemView.findViewById(R.id.user_display_layout_user_name);
            userEmail = itemView.findViewById(R.id.user_display_layout_user_email);

            accept = itemView.findViewById(R.id.user_display_layout_accept_button);
            reject = itemView.findViewById(R.id.user_display_layout_reject_button);
        }
    }
}
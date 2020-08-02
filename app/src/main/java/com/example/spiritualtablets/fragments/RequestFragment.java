package com.example.spiritualtablets.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiritualtablets.R;
import com.example.spiritualtablets.models.Contacts;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    View requestFragmentView;
    RecyclerView requestRV;
    DatabaseReference chatReqRef, userRef, contactRef;
    FirebaseAuth mAuth;
    String currentUserId;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestFragmentView = inflater.inflate(R.layout.fragment_request, container, false);

        requestRV = requestFragmentView.findViewById(R.id.request_list_recycler_view);
        requestRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();

        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        chatReqRef = FirebaseDatabase.getInstance().getReference("chat_requests");
        contactRef = FirebaseDatabase.getInstance().getReference("contacts");

        return requestFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatReqRef.child(currentUserId), Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, RequestViewHolder> adapter =  new FirebaseRecyclerAdapter<Contacts, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {

                //holder.accept.setVisibility(View.VISIBLE);
                //holder.reject.setVisibility(View.VISIBLE);

                final String user_id  = getRef(position).getKey();


                DatabaseReference request_type_ref = getRef(position).child("request_type").getRef();

                request_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            String request_type = Objects.requireNonNull(snapshot.getValue()).toString();

                            if (request_type.equals("received")){

                                userRef = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
                                userRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()){

                                            holder.userEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                                            holder.userName.setText(Objects.requireNonNull(snapshot.child("full_name").getValue()).toString());
                                            Picasso.get().load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(holder.userImage);

                                        }

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                CharSequence[] options = new CharSequence[]{ "Accept", "Reject"};

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Accept or Reject Request");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (which == 0){

                                                            contactRef.child(currentUserId).child(user_id).child("contact")
                                                                    .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    contactRef.child(user_id).child(currentUserId).child("contact")
                                                                            .setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            chatReqRef.child(currentUserId)
                                                                                    .child(user_id)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            chatReqRef.child(user_id)
                                                                                                    .child(currentUserId)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
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

                                                            chatReqRef.child(currentUserId)
                                                                    .child(user_id)
                                                                    .removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            chatReqRef.child(user_id)
                                                                                    .child(currentUserId)
                                                                                    .removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            Toast.makeText(getContext(), "Request Rejected", Toast.LENGTH_SHORT).show();
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
                View view = LayoutInflater.from(getContext()).inflate(R.layout.users_display_layout, parent, false);

                return new RequestViewHolder(view);
            }
        };

        requestRV.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{


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

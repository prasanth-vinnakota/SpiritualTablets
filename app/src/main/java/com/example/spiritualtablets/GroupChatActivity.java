package com.example.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spiritualtablets.adapters.MessageAdapter;
import com.example.spiritualtablets.models.MessageItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton sendMessageButton;
    private EditText userMessageInput;
    private String currentGroupName;
    private FirebaseAuth mAuth;
    private String currentUserId, currentUserName, currentDate, currentTime;
    private DatabaseReference user_ref, group_ref, group_message_key_ref;
    private RecyclerView groupChatRV;
    private List<MessageItem> messageItems;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        toolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(toolbar);
        currentGroupName = getIntent().getStringExtra("group_name");
        Objects.requireNonNull(getSupportActionBar()).setTitle(currentGroupName);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        currentUserName = mAuth.getCurrentUser().getDisplayName();
        group_ref = FirebaseDatabase.getInstance().getReference("groups").child(currentGroupName);

        sendMessageButton = findViewById(R.id.send_group_message_button);
        userMessageInput = findViewById(R.id.input_group_message);
        groupChatRV = findViewById(R.id.group_chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        groupChatRV.setLayoutManager(linearLayoutManager);

        group_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);
                    messageItems.add(messageItem);
                }
                messageAdapter = new MessageAdapter(getApplicationContext(), messageItems);
                groupChatRV.setAdapter(messageAdapter);
                if (messageItems.size() > 1)
                    groupChatRV.smoothScrollToPosition(messageItems.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMessageToDatabase();

                userMessageInput.setText("");
            }
        });
    }

    private void saveMessageToDatabase() {

        String message = userMessageInput.getText().toString();
        String messageKey = group_ref.push().getKey();

        if (TextUtils.isEmpty(message)) {

            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
            return;
        }

        assert messageKey != null;
        group_message_key_ref = group_ref.child(messageKey);

        MessageItem messageItem = new MessageItem(message, currentUserName, currentUserId);

        group_message_key_ref.setValue(messageItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                userMessageInput.setText("");
            }
        });


    }

}

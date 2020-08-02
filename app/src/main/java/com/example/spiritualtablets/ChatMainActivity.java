package com.example.spiritualtablets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spiritualtablets.adapters.TabsAccessoryAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ChatMainActivity extends AppCompatActivity {

    private DatabaseReference groups_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        Toolbar toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        groups_ref = FirebaseDatabase.getInstance().getReference("groups");

        ViewPager myViewPager = findViewById(R.id.main_tab_pages);
        TabsAccessoryAdapter myTabsAccessoryAdapter = new TabsAccessoryAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessoryAdapter);

        TabLayout myTabLayout = findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_find_friends_option){

            Intent intent = new Intent(ChatMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.main_settings_option){

            Intent intent = new Intent(ChatMainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.main_create_new_group_option){

            createNewGroup();

        }
        return true;
    }

    private void createNewGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter Group Name");
        final EditText  groupName = new EditText(this);
        groupName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        groupName.setHint("Group Name");
        builder.setView(groupName);
        builder.setCancelable(false);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String group_name = groupName.getText().toString();
                
                if (TextUtils.isEmpty(group_name)){
                    Toast.makeText(ChatMainActivity.this, "Please enter group name", Toast.LENGTH_SHORT).show();
                    return;
                }

                groups_ref.child(group_name).setValue("");
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}

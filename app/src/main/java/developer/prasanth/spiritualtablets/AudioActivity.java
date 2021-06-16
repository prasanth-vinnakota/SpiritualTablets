package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.AudioRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.ItemBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AudioActivity extends AppCompatActivity {
    List<ItemBean> itemBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String language = getIntent().getStringExtra("language");

        RecyclerView recyclerView = findViewById(R.id.audio_recycler_view_id);
        DatabaseReference audios_ref = FirebaseDatabase.getInstance().getReference("Audios").child(language);
        audios_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemBeans = new ArrayList<>();
                if (check)
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ItemBean itemBean = new ItemBean();
                        itemBean.setName(dataSnapshot.child("name").getValue().toString());
                        itemBean.setLink(dataSnapshot.child("link").getValue().toString());
                        itemBeans.add(itemBean);
                    }
                    check = false;
                    recyclerView.setLayoutManager(new LinearLayoutManager(AudioActivity.this));
                    AudioRecyclerViewAdapter myAdapter = new AudioRecyclerViewAdapter(AudioActivity.this,itemBeans,language);
                    recyclerView.setAdapter(myAdapter);
                }else{
                    showMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AudioActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    private void showMessage(){

        AlertDialog.Builder builder = new AlertDialog.Builder(AudioActivity.this);
        builder.setMessage("No Audios Available");
        builder.setCancelable(true);
        builder.create().show();
    }
}

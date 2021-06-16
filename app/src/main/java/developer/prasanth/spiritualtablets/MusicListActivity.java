package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import developer.prasanth.spiritualtablets.adapters.MusicMeditationAdapter;
import developer.prasanth.spiritualtablets.models.ItemBean;

public class MusicListActivity extends AppCompatActivity {

    List<ItemBean> itemBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(MusicListActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String time = getIntent().getStringExtra("time");
        String title = time + " Min Music";
        TextView titleTv = findViewById(R.id.activity_music_list_title);
        titleTv.setText(title);

        RecyclerView recyclerView = findViewById(R.id.activity_music_list_RV);
        DatabaseReference music_ref = FirebaseDatabase.getInstance().getReference("Music").child(time);
        music_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemBeans = new ArrayList<>();
                if (check)
                    if (snapshot.exists()) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ItemBean itemBean = new ItemBean();
                            itemBean.setLink(dataSnapshot.child("link").getValue().toString());
                            itemBean.setName(dataSnapshot.child("name").getValue().toString());
                            itemBeans.add(itemBean);
                        }
                        check = false;
                        recyclerView.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
                        MusicMeditationAdapter musicMeditationAdapter = new MusicMeditationAdapter(MusicListActivity.this,itemBeans,time);
                        recyclerView.setAdapter(musicMeditationAdapter);
                    }
                else{
                        Toast.makeText(MusicListActivity.this, "No Music Available", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}
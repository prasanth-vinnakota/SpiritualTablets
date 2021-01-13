package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EnglishBookListActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(EnglishBookListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(EnglishBookListActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dataItems =new ArrayList<>();
        dataItems.add(new DataItem("Meditation", "English", R.drawable.meditation));
        dataItems.add(new DataItem("Lakshmi Paravathi Saraswathi", "English", R.drawable.lakshmi_parvathi_sarswathi));
        dataItems.add(new DataItem("Direct And Indirect Knowledge", "English", R.drawable.direct_and_indirect_knowledge));
        dataItems.add(new DataItem("Purpose of Life", "English", R.drawable.purpose_of_life));
        dataItems.add(new DataItem("Six Passions", "English", R.drawable.six_passions));
        dataItems.add(new DataItem("Soul Family", "English", R.drawable.soul_family));
        dataItems.add(new DataItem("Thyroid", "English", R.drawable.thyriod));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(EnglishBookListActivity.this, dataItems);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(myAdapter);
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
}

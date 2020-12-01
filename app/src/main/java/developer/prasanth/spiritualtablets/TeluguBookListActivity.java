package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeluguBookListActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(TeluguBookListActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon");
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    if (Objects.requireNonNull(snapshot.getValue()).toString().equalsIgnoreCase("true"))
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(TeluguBookListActivity.this, R.drawable.gradient_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dataItems = new ArrayList<>();
        dataItems.add(new DataItem("Dhyanam", "Telugu", R.drawable.dhyanam));
        dataItems.add(new DataItem("Lakshmi Paravathi Saraswathi", "Telugu", R.drawable.lakshmi_parvathi_sarswathi_telugu));
        dataItems.add(new DataItem("12 Tablets", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Aadhyathmika Putrulu", "Telugu", R.drawable.adyatmika_putrulu));
        dataItems.add(new DataItem("Yeadu Shakthi Kendralu", "Telugu", R.drawable.yeadu_sakthi_kendralu));
        dataItems.add(new DataItem("Aacharya Saangatyam", "Telugu", R.drawable.acharya_sangatyam));
        dataItems.add(new DataItem("Mudu Rakala Sevalu", "Telugu", R.drawable.mudu_rakala_sevalu));
        dataItems.add(new DataItem("Nalugu Setruvulu", "Telugu", R.drawable.nalugu_setruvulu));
        dataItems.add(new DataItem("Arishadvargalu", "Telugu", R.drawable.arishadvargaalu));
        dataItems.add(new DataItem("Nissahayata", "Telugu", R.drawable.nissahayatha));
        dataItems.add(new DataItem("Prathyaksha Gnanam Paroksha Gnanam", "Telugu", R.drawable.prathyaksha_gnanam_paroksha_gnanam));
        dataItems.add(new DataItem("Dhayna Vidya Sanjeevini Vidya", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Aathma Kutumba Dharmam", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Chalakudi nundi Saasanudu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Jeevitha Dheyeyam", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Mudu Gunaalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Yogaparampara", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Paraspara Sambandhalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Pancha Bhutalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("5 Saamrajyaalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Pidikili", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("5 Pedda Tappulu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("2 Tablets", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Athma Scanning", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Nijamaina Seva", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Samasya Aathyathmika Pariskaram", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Gnaname Avushadamu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Mudu Dharmaalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("7 Anubhavaalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Adrushtam Duradrushtam", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Dhyana Jeevitham", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Garbaasayam", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Manchi Kashtalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Shaktivalayalu", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Sugar", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Swadyayam", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Thyroid", "Telugu", R.drawable.book_cover_page));
        dataItems.add(new DataItem("Traffic Signals", "Telugu", R.drawable.book_cover_page));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(TeluguBookListActivity.this, dataItems);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(myAdapter);
    }

}

package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;

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

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.ItemBean;

public class BooksActivity extends AppCompatActivity {

    List<ItemBean> itemBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(BooksActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    getWindow().getDecorView().setBackground(ContextCompat.getDrawable(BooksActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String language = getIntent().getStringExtra("language");

        RecyclerView recyclerView = findViewById(R.id.books_recycler_view_id);
        itemBeans = new ArrayList<>();

        DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("Books").child(language);
        books_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check)
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ItemBean itemBean = new ItemBean();
                        itemBean.setName(dataSnapshot.child("name").getValue().toString());
                        itemBean.setLink(dataSnapshot.child("link").getValue().toString());
                        itemBeans.add(itemBean);
                    }

                    check = false;
                    recyclerView.setLayoutManager(new LinearLayoutManager(BooksActivity.this));
                    BooksRecyclerViewAdapter booksRecyclerViewAdapter = new BooksRecyclerViewAdapter(BooksActivity.this, itemBeans, language);
                    recyclerView.setAdapter(booksRecyclerViewAdapter);

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
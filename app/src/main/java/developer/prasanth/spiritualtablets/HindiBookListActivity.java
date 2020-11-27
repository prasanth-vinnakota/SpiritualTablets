package developer.prasanth.spiritualtablets;


import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;

public class HindiBookListActivity extends AppCompatActivity {
    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dataItems =new ArrayList<>();
        dataItems.add(new DataItem("12 Tablets", "Hindi", R.drawable.book_cover_page));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(getApplicationContext(), dataItems);
        recyclerView.setLayoutManager(new GridLayoutManager(HindiBookListActivity.this,1));
        recyclerView.setAdapter(myAdapter);
    }
}

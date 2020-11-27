package developer.prasanth.spiritualtablets;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;

public class KannadaBookListActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dataItems =new ArrayList<>();
        dataItems.add(new DataItem("Direct and Indirect Knowledge", "Kannada", R.drawable.direct_and_indirect_knowledge));
        dataItems.add(new DataItem("Lakshmi Paravathi Saraswathi", "Kannada", R.drawable.lakshmi_parvathi_sarswathi));
        dataItems.add(new DataItem("Meditation", "Kannada", R.drawable.meditation));
        dataItems.add(new DataItem("Swadhyaya", "Kannada", R.drawable.book_cover_page));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(KannadaBookListActivity.this, dataItems);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setAdapter(myAdapter);
    }
}

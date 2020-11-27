package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import developer.prasanth.spiritualtablets.adapters.BooksRecyclerViewAdapter;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;

public class EnglishBookListActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
}

package com.example.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.spiritualtablets.models.DataItem;

import java.util.ArrayList;
import java.util.List;

public class TeluguBookListActivity extends AppCompatActivity {
    List<DataItem> lstDataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        lstDataItem = new ArrayList<>();
        lstDataItem.add(new DataItem("Dhyanam", "Telugu", R.drawable.dhyanam));
        lstDataItem.add(new DataItem("Lakshmi Paravathi Saraswathi", "Telugu", R.drawable.lakshmi_parvathi_sarswathi_telugu));
        lstDataItem.add(new DataItem("12 Tablets", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Aadhyathmika Putrulu", "Telugu", R.drawable.adyatmika_putrulu));
        lstDataItem.add(new DataItem("Yeadu Shakthi Kendralu", "Telugu", R.drawable.yeadu_sakthi_kendralu));
        lstDataItem.add(new DataItem("Aacharya Saangatyam", "Telugu", R.drawable.acharya_sangatyam));
        lstDataItem.add(new DataItem("Mudu Rakala Sevalu", "Telugu", R.drawable.mudu_rakala_sevalu));
        lstDataItem.add(new DataItem("Nalugu Setruvulu", "Telugu", R.drawable.nalugu_setruvulu));
        lstDataItem.add(new DataItem("Arishadvargalu", "Telugu", R.drawable.arishadvargaalu));
        lstDataItem.add(new DataItem("Nissahayata", "Telugu", R.drawable.nissahayatha));
        lstDataItem.add(new DataItem("Prathyaksha Gnanam Paroksha Gnanam", "Telugu", R.drawable.prathyaksha_gnanam_paroksha_gnanam));
        lstDataItem.add(new DataItem("Dhayna Vidya Sanjeevini Vidya", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Aathma Kutumba Dharmam", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Chalakudi nundi Saasanudu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Jeevitha Dhyeyam", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Mudu Gunaalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Yogaparampara", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Paraspara Sambandhalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Pancha Bhutalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("5 Saamrajyaalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Pidikili", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("5 Pedda Tappulu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("2 Tablets", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Athma Scanning", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Nijamaina Seva", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Samasya Aathyathmika Pariskaram", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Gnaname Avushadamu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Mudu Dharmaalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("7 Anubhavaalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Adrushtam Duradrushtam", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Dhyana Jeevitham", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Garbaasayam", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Manchi Kashtalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Shaktivalayalu", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Sugar", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Swadyayam", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Thyroid", "Telugu", R.drawable.book_cover_page));
        lstDataItem.add(new DataItem("Traffic Signals", "Telugu", R.drawable.book_cover_page));

        RecyclerView myrv = findViewById(R.id.recycler_view_id);
        BooksRecyclerViewAdapter myAdapter = new BooksRecyclerViewAdapter(TeluguBookListActivity.this, lstDataItem);
        myrv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        myrv.setAdapter(myAdapter);
    }
}

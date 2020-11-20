package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import developer.prasanth.spiritualtablets.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ViewBookActivity extends AppCompatActivity {

    private static PDFView pdfView;
    static LoadingDialog loadingDialog;
    boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pdfView = findViewById(R.id.pdf_view);

        check();

    }

    private void check() {

        if (getIntent().getStringExtra("language").equals("English")){

            if (getIntent().getStringExtra("book_name").equals("Meditation") | getIntent().getStringExtra("book_name").equals("Lakshmi Paravathi Saraswathi")){

                check = true;
                openBook();
                return;
            }
        }
        if (getIntent().getStringExtra("language").equals("Telugu")){

            if (getIntent().getStringExtra("book_name").equals("Dhyanam") | getIntent().getStringExtra("book_name").equals("Lakshmi Paravathi Saraswathi") | getIntent().getStringExtra("book_name").equals("12 Tablets") | getIntent().getStringExtra("book_name").equals("Aadhyathmika Putrulu") | getIntent().getStringExtra("book_name").equals("Aacharya Saangatyam") | getIntent().getStringExtra("book_name").equals("Yeadu Shakthi Kendralu")){

                check = true;
                openBook();
                return;
            }
        }

        if(getIntent().getStringExtra("language").equals("Hindi") | getIntent().getStringExtra("language").equals("Kannada") ){

            check = true;
            openBook();
            return;
        }


        DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("books");

        books_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("yes")){
                    check = true;
                }
                openBook();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void openBook(){


        if (check) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books").child(Objects.requireNonNull(getIntent().getStringExtra("language"))).child(getIntent().getStringExtra("book_name"));

            loadingDialog = new LoadingDialog(this);
            loadingDialog.startLoading();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String url = dataSnapshot.getValue(String.class);

                    new RetrievePdfStream().execute(url);
                    Toast.makeText(ViewBookActivity.this, "Please Wait It May Take More Than A Minute", Toast.LENGTH_LONG).show();

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(ViewBookActivity.this);
                    builder.setTitle("Select One");
                    builder.setPositiveButton("download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            dialogInterface.dismiss();
                            Toast.makeText(ViewBookActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("view", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            new RetrievePdfStream().execute(url);
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Access Denied");
            builder.setMessage("To read this book you need to do at least one of the following.\n" +
                    "1.Spiritual Counselling : fill Request Counselling form in Patient Registration\n" +
                    "2.Become a Volunteer : fill Volunteer form in Volunteer Registration\n" +
                    "3.Become a Donor : donate amount in Donation");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

    static class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            loadingDialog.dismiss();
        }
    }
}

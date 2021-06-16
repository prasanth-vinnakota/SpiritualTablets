package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ViewBookActivity extends AppCompatActivity {

    boolean check = false;
    WebView pdfView;
    String language;
    String bookName;
    String stringUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        check();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {

        pdfView = findViewById(R.id.web_view_for_view_book);
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.getSettings().setDisplayZoomControls(false);

        stringUrl = getIntent().getStringExtra("url");
        bookName = getIntent().getStringExtra("book_name");
        language = getIntent().getStringExtra("language");
    }

    private void check() {

        if (language.equals("English")) {

            if (bookName.equals("Meditation") | bookName.equals("Lakshmi Paravathi Saraswathi")) {

                check = true;
                openBook();
                return;
            }
        }
        if (language.equals("Telugu")) {

            if (bookName.equals("ధ్యానం") | bookName.equals("లక్ష్మి పార్వతి సరస్వతి") | bookName.equals("పన్నెండు ఆధ్యాత్మిక టాబ్లెట్లు") | bookName.equals("ఆధ్యాత్మిక పుత్రులు") | bookName.equals("ఆచార్య సాంగత్యం") | bookName.equals("ఏడు శక్తి కేంద్రాలు")) {

                check = true;
                openBook();
                return;
            }
        }

        if (language.equals("Hindi") | language.equals("Kannada")) {

            check = true;
            openBook();
            return;
        }


        DatabaseReference booksReference = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("books");

        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (Objects.requireNonNull(snapshot.getValue()).toString().equals("yes")) {
                    check = true;
                }
                openBook();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void openBook() {
        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (check) {
            DatabaseReference user_books_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("Books").child(bookName);
            user_books_ref.addValueEventListener(new ValueEventListener() {
                boolean check = true;

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (check) {
                        long count = snapshot.getChildrenCount();
                        count += 1;
                        user_books_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                    }
                    check = false;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("Books").child(language).child(bookName).child("Users").child(current_user_id);
            books_ref.addValueEventListener(new ValueEventListener() {
                boolean check = true;

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (check) {
                        long count = snapshot.getChildrenCount();
                        count++;
                        books_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                    }
                    check = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewBookActivity.this);
            builder.setTitle(R.string.loading);
            builder.setCancelable(true);
            builder.setMessage("If books are not opening press back button and open the book again");

            pdfView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    builder.create().show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pdfView.loadUrl("javascript:(function() { " +
                            "document.querySelector('[role=\"toolbar\"]').remove();})()");
                }
            });
            String url = "";
            try {
                url = URLEncoder.encode(stringUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                showMessage(e.getMessage());
            }
            pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
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

    private void showMessage(String message) {

        Toast.makeText(ViewBookActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private String getDateAndTime() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar).toString();
    }

}

package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class NewsLettersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letters);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(NewsLettersActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(NewsLettersActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void july2018(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://spiritualtablet.org/images/data/NewsLetters/Newsletter%20July.pdf"));
        startActivity(intent);
    }

    public void january2018(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://spiritualtablet.org/images/data/NewsLetters/News%20Bulition%20december.pdf"));
        startActivity(intent);
    }

    public void september2016(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/t/ViewEmail/j/BC55DC689591CD19/C67FD2F38AC4859C/"));
        startActivity(intent);
    }

    public void may2016(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pssm.cmail20.com/t/ViewEmail/j/5F154942F8F1C9AF"));
        startActivity(intent);
    }

    public void january2016(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pssm.cmail20.com/t/ViewEmail/j/EFA3E689FA61EBA8"));
        startActivity(intent);
    }

    public void october2015(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pssm.cmail2.com/t/ViewEmail/j/2D5732E2A7F2D379"));
        startActivity(intent);
    }

    public void august2015(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pssm.cmail2.com/t/ViewEmail/j/2075529D26AD6F5A"));
        startActivity(intent);
    }

    public void july2015(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://pssm.createsend1.com/t/ViewEmail/j/CC28E4B84FC62108"));
        startActivity(intent);
    }

    public void april2015(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=01FDF1C41DEBE889&temp=False"));
        startActivity(intent);
    }

    public void january2015(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=CA465937085D868B&temp=False"));
        startActivity(intent);
    }

    public void september2014(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=214BE9659DBAEF9D&temp=False"));
        startActivity(intent);
    }

    public void july2014(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=AE5453F12DC9BE0A&temp=False"));
        startActivity(intent);
    }

    public void july2013(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=586BBFE397B97AFD&temp=False"));
        startActivity(intent);
    }

    public void may2013(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=8A4ED2DE64EBDE4F&temp=False"));
        startActivity(intent);
    }

    public void december2012(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://dhyanarogyam.createsend.com/t/ViewEmail/t/3BEE6D26863BA124/C67FD2F38AC4859C/"));
        startActivity(intent);
    }

    public void february2014(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://pssm.createsend.com/campaigns/reports/viewCampaign.aspx?d=j&c=B70A9281B84B04D3&ID=02434440C7F95B81&temp=False"));
        startActivity(intent);

    }

    public void april2013(View view) {

        showMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://phsc.createsend4.com/t/ViewEmail/t/7EAFAE749423D972"));
        startActivity(intent);
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    void showMessage() {

        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
    }
}

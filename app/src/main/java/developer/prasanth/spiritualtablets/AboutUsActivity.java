package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

import developer.prasanth.spiritualtablets.about_us.FounderActivity;
import developer.prasanth.spiritualtablets.about_us.HistoryActivity;
import developer.prasanth.spiritualtablets.about_us.IntroductionActivity;
import developer.prasanth.spiritualtablets.about_us.PrincipleActivity;
import developer.prasanth.spiritualtablets.about_us.PyramidDoctorsActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void introduction(View view) {

        startActivity(new Intent(this, IntroductionActivity.class));
    }

    public void history(View view) {

        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void principle(View view) {

        startActivity(new Intent(this, PrincipleActivity.class));
    }

    public void pyramidDoctors(View view) {

        startActivity(new Intent(this, PyramidDoctorsActivity.class));
    }

    public void outlets(View view) {

        startActivity(new Intent(this, OutletsActivity.class));
    }

    public void founder(View view) {

        startActivity(new Intent(this, FounderActivity.class));
    }

    public void counsellors(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://spiritualtablet.org/testmonials.html"));
        startActivity(intent);
    }

}

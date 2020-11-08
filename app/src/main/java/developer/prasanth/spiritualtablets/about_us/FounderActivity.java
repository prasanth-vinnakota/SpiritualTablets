package developer.prasanth.spiritualtablets.about_us;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.VideoPlayerActivity;

public class FounderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder);
    }

    public void loadGkSirSpeech(View view) {
        Intent intent = new Intent(FounderActivity.this, VideoPlayerActivity.class);
        intent.putExtra("name", "Dr GK Spiritual Tablets");
        startActivity(intent);

    }
}
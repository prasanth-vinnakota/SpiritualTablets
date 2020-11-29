package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = findViewById(R.id.activity_full_screen_image_image_view);

        Intent intent = getIntent();
        if (intent != null){
            Uri uri = intent.getData();
            if (uri != null && imageView != null){
                Glide.with(FullScreenImageActivity.this)
                        .load(uri)
                        .into(imageView);
            }
        }
    }
}
package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    Animation topAnim;
    Animation bottomAnim;
    TextView appName;
    TextView appName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        int SPLASH_TIME_OUT = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(appName, "home_image");
                pairs[1] = new Pair<View, String>(appName2, "home_text");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }, SPLASH_TIME_OUT);
    }

    private void init(){

        topAnim = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        appName2 = findViewById(R.id.textView2);
        appName = findViewById(R.id.nav_user_email);

        appName.setAnimation(topAnim);
        appName2.setAnimation(bottomAnim);
    }
}

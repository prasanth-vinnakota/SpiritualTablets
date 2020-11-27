package developer.prasanth.spiritualtablets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
    }

    public void load(View view) {

        if (checkInternetConnection())
            startActivity(new Intent(NoInternetActivity.this, LoggedInActivity.class));
        else
            showMessage();
    }

    public boolean checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        if (connectivityManager != null) {

            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        return (mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected());
    }

    private void showMessage(){
        Toast.makeText(NoInternetActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
    }
}
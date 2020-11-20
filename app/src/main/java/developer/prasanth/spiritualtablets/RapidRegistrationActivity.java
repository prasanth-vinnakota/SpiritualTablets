package developer.prasanth.spiritualtablets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RapidRegistrationActivity extends AppCompatActivity {


    EditText firstName, lastName, age, mobile, email, address, education, disease, motherTongue, referredByName, referredByMobile;
    RadioGroup gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_registration);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firstName = findViewById(R.id.rapid_registration_first_name);
        lastName = findViewById(R.id.rapid_registration_last_name);
        age = findViewById(R.id.rapid_registration_age);
        mobile = findViewById(R.id.rapid_registration_mobile);
        email = findViewById(R.id.rapid_registration_email);
        email.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        address = findViewById(R.id.rapid_registration_address);
        education = findViewById(R.id.rapid_registration_education);
        disease = findViewById(R.id.rapid_registration_disease);
        motherTongue = findViewById(R.id.rapid_registration_mother_tongue);
        referredByName = findViewById(R.id.rapid_registration_referred_by_name);
        referredByMobile = findViewById(R.id.rapid_registration_referred_by_mobile);

        gender = findViewById(R.id.rapid_registration_gender);
    }

    private String getGender(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.rapid_registration_male:
                return "Male";
            case R.id.rapid_registration_female:
                return "Female";
            case R.id.rapid_registration_other_gender:
                return "Other";
        }

        return "Male";
    }

    public void submit(View view) {

        String patient_first_name = firstName.getText().toString();
        String patient_last_name = lastName.getText().toString();
        String patient_age = age.getText().toString();
        String patient_mobile = mobile.getText().toString();
        String patient_address = address.getText().toString();
        String patient_education = education.getText().toString();
        String patient_disease = disease.getText().toString();
        String patient_mother_tongue = motherTongue.getText().toString();
        final String patient_email = email.getText().toString();
        String patient_referred_by_name = referredByName.getText().toString();
        String patient_referred_by_mobile = referredByMobile.getText().toString();

        int patient_gender = gender.getCheckedRadioButtonId();

        if (TextUtils.isEmpty(patient_first_name)) {
            firstName.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_last_name)) {
            lastName.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_age)) {
            age.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_mobile)) {
            mobile.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_email)) {
            email.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_address)) {
            address.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_education)) {
            education.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_disease)) {
            disease.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_mother_tongue)) {
            motherTongue.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_referred_by_name)) {
            referredByName.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_referred_by_mobile)) {
            referredByMobile.setError("required field");
            return;
        }

        DatabaseReference rapid_registration_ref = FirebaseDatabase.getInstance().getReference("rapid_registration").push();
        DatabaseReference unchecked_rapid_registration_ref = FirebaseDatabase.getInstance().getReference("unchecked_rapid_registration").child(Objects.requireNonNull(rapid_registration_ref.getKey()));
        unchecked_rapid_registration_ref.setValue(true);

        final Map<String, Object> map = new HashMap<>();
        map.put("first_name", patient_first_name);
        map.put("last_name", patient_last_name);
        map.put("age", patient_age);
        map.put("mobile", patient_mobile);
        map.put("address", patient_address);
        map.put("education", patient_education);
        map.put("disease", patient_disease);
        map.put("mother_tongue", patient_mother_tongue);
        map.put("email", patient_email);
        map.put("referred_by_name", patient_referred_by_name);
        map.put("referred_by_mobile", patient_referred_by_mobile);
        map.put("gender", getGender(patient_gender));
        map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        rapid_registration_ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {

                    DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("books");
                    books_ref.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(RapidRegistrationActivity.this);
                            builder.setTitle("Patient Registered Successfully");
                            builder.setMessage("We will contact you soon");
                            builder.setCancelable(false);
                            builder.setPositiveButton("dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(RapidRegistrationActivity.this, DashBoardActivity.class));
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
            }
        });
    }
}
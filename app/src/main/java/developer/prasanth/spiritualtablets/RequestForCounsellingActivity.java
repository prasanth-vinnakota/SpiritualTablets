package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class RequestForCounsellingActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText age;
    EditText mobile;
    EditText email;
    EditText disease;
    RadioGroup gender, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_counselling);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RequestForCounsellingActivity.this, R.drawable.background_gradient));

        DatabaseReference fullMoonReference = FirebaseDatabase.getInstance().getReference("full_moon_days").child(getDate());
        fullMoonReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                        getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RequestForCounsellingActivity.this, R.drawable.full_moon_background));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        inti();
    }

    private String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }

    private void inti() {

        firstName = findViewById(R.id.request_for_counselling_first_name);
        lastName = findViewById(R.id.request_for_counselling_last_name);
        age = findViewById(R.id.request_for_counselling_age);
        mobile = findViewById(R.id.request_for_counselling_mobile);
        email = findViewById(R.id.request_for_counselling_email);
        disease = findViewById(R.id.request_for_counselling_disease);
        gender = findViewById(R.id.request_for_counselling_gender);
        mode = findViewById(R.id.request_for_counselling_mode);
    }

    @SuppressLint("NonConstantResourceId")
    private String getGender(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.request_for_counselling_male:
                return "Male";
            case R.id.request_for_counselling_female:
                return "Female";
            case R.id.request_for_counselling_other_gender:
                return "Other";
        }

        return "Male";
    }

    @SuppressLint("NonConstantResourceId")
    private String getMode(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.request_for_counselling_phone_mode:
                return "Phone";
            case R.id.request_for_counselling_email_mode:
                return "Email";
            case R.id.request_for_counselling_online_mode:
                return "Online";
            case R.id.request_for_counselling_home_visit_mode:
                return "Home Visit";
            case R.id.request_for_counselling_center_mode:
                return "Center";
        }
        return "Phone";
    }

    public void submit(View view) {

        String patient_first_name = firstName.getText().toString();
        String patient_last_name = lastName.getText().toString();
        String patient_age = age.getText().toString();
        String patient_mobile = mobile.getText().toString();
        String patient_disease = disease.getText().toString();
        final String patient_email = email.getText().toString();

        int patient_gender = gender.getCheckedRadioButtonId();
        int patient_mode = mode.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(patient_first_name)) {

            firstName.setError("required field");
            showMessage("Required Filed Is Missing");
            return;
        }

        if (TextUtils.isEmpty(patient_last_name)) {

            lastName.setError("required field");
            showMessage("Required Filed Is Missing");
            return;
        }

        if (TextUtils.isEmpty(patient_age)) {

            age.setError("required field");
            showMessage("Required Filed Is Missing");
            return;
        }

        if (TextUtils.isEmpty(patient_mobile)) {

            mobile.setError("required field");
            showMessage("Required Filed Is Missing");
            return;
        } else {
            if (patient_mobile.charAt(0) != '+')
                patient_mobile = "+" + patient_mobile;
        }


        if (TextUtils.isEmpty(patient_disease)) {

            disease.setError("required field");
            showMessage("Required Filed Is Missing");
            return;
        }

        DatabaseReference requestFoCounsellingReference = FirebaseDatabase.getInstance().getReference("request_for_counselling").push();
        DatabaseReference uncheckedRequestForCounsellingReference = FirebaseDatabase.getInstance().getReference("unchecked_request_for_counselling").child(Objects.requireNonNull(requestFoCounsellingReference.getKey()));
        uncheckedRequestForCounsellingReference.setValue(true);

        final Map<String, Object> map = new HashMap<>();
        map.put("first_name", patient_first_name);
        map.put("last_name", patient_last_name);
        map.put("age", patient_age);
        map.put("mobile", patient_mobile);
        map.put("disease", patient_disease);
        if (!TextUtils.isEmpty(patient_email))
            map.put("email", patient_email);
        map.put("mode", getMode(patient_mode));
        map.put("gender", getGender(patient_gender));
        map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        requestFoCounsellingReference.updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isComplete()) {

                            DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("books");
                            books_ref.setValue("yes")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestForCounsellingActivity.this);
                                            builder.setTitle("Patient Registered Successfully");
                                            builder.setMessage("We will contact you soon");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("dismiss", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(RequestForCounsellingActivity.this, DashBoardActivity.class));
                                                    finish();
                                                }
                                            });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            showMessage(e.getMessage());
                                        }
                                    });
                        } else
                            showMessage(Objects.requireNonNull(task.getException()).getMessage());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showMessage(e.getMessage());
                    }
                });
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestForCounsellingActivity.this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }
}
package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestForCounsellingActivity extends AppCompatActivity {

    EditText firstName, lastName, age, mobile, email, disease;
    RadioGroup gender, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_counselling);

        firstName = findViewById(R.id.request_for_counselling_first_name);
        lastName = findViewById(R.id.request_for_counselling_last_name);
        age = findViewById(R.id.request_for_counselling_age);
        mobile = findViewById(R.id.request_for_counselling_mobile);
        email = findViewById(R.id.request_for_counselling_email);
        disease = findViewById(R.id.request_for_counselling_disease);

        gender = findViewById(R.id.request_for_counselling_gender);
        mode = findViewById(R.id.request_for_counselling_mode);
    }


    private String getGender (int checkedRadioButtonId){
        switch (checkedRadioButtonId){
            case R.id.request_for_counselling_male:
                return "Male";
            case R.id.request_for_counselling_female:
                return "Female";
            case R.id.request_for_counselling_other_gender:
                return "Other";
        }

        return "Male";
    }

    private String getMode (int checkedRadioButtonId){
        switch (checkedRadioButtonId){
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
        String patient_email = email.getText().toString();

        int patient_gender = gender.getCheckedRadioButtonId();
        int patient_mode = mode.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(patient_first_name)){
            firstName.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_last_name)){
            lastName.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_age)){
            age.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_mobile)){
            mobile.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_email)){
            email.setError("required field");
            return;
        }

        if (TextUtils.isEmpty(patient_disease)){
            disease.setError("required field");
            return;
        }

        DatabaseReference request_for_counselling_ref = FirebaseDatabase.getInstance().getReference("request_for_counselling").push();
        DatabaseReference unchecked_request_for_counselling_ref = FirebaseDatabase.getInstance().getReference("unchecked_request_for_counselling").child(Objects.requireNonNull(request_for_counselling_ref.getKey()));
        unchecked_request_for_counselling_ref.setValue(true);

        Map<String, Object> map = new HashMap<>();
        map.put("first_name",patient_first_name);
        map.put("last_name", patient_last_name);
        map.put("age",patient_age);
        map.put("mobile", patient_mobile);
        map.put("disease",patient_disease);
        map.put("email",patient_email);
        map.put("mode",getMode(patient_mode));
        map.put("gender",getGender(patient_gender));

        request_for_counselling_ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("books");
                    books_ref.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestForCounsellingActivity.this);
                            builder.setTitle("Patient Registered Successfully");
                            builder.setMessage("We will contact you soon");
                            builder.setCancelable(false);
                            builder.setPositiveButton("dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(RequestForCounsellingActivity.this,DashBoardActivity.class));
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
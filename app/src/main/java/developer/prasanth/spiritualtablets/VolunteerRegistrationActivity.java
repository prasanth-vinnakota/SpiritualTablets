package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VolunteerRegistrationActivity extends Activity {

    EditText mailId;
    EditText name;
    EditText address;
    EditText phone;
    EditText communicateTime;
    EditText comment;
    EditText otherTiming;
    EditText otherContribute;
    RadioGroup wayOfContribution;
    RadioGroup timeToContribute;
    CheckBox monday;
    CheckBox tuesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;
    CheckBox saturday;
    CheckBox sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_registration);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        wayOfContribution.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (wayOfContribution.getCheckedRadioButtonId() == R.id.others_contribute) {
                    otherContribute.setVisibility(View.VISIBLE);
                } else {
                    otherContribute.setVisibility(View.GONE);
                }
            }
        });

        timeToContribute.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (timeToContribute.getCheckedRadioButtonId() == R.id.others_timing) {
                    otherTiming.setVisibility(View.VISIBLE);
                } else {
                    otherTiming.setVisibility(View.GONE);
                }
            }
        });
    }

    private void init(){

        mailId = findViewById(R.id.volunteer_email);
        name = findViewById(R.id.volunteer_name);
        address = findViewById(R.id.volunteer_address);
        phone = findViewById(R.id.volunteer_mobile);
        communicateTime = findViewById(R.id.volunteer_best_time_to_contact);
        comment = findViewById(R.id.volunteer_comments);
        otherContribute = findViewById(R.id.others_contribute_edit_text);
        otherTiming = findViewById(R.id.others_timing_edit_text);
        mailId.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null)
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        wayOfContribution = findViewById(R.id.volunteer_way_of_contribution);
        timeToContribute = findViewById(R.id.volunteer_time_to_contribute);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);

    }

    public void submit(View view) {

        final String volunteer_mail = mailId.getText().toString();
        final String volunteer_name = name.getText().toString();
        final String volunteer_phone = phone.getText().toString();
        final String volunteer_address = address.getText().toString();
        final String volunteer_communicate_time = communicateTime.getText().toString();
        final String volunteer_comment = comment.getText().toString();

        int radio_id_contribution = wayOfContribution.getCheckedRadioButtonId();
        int radio_id_time = timeToContribute.getCheckedRadioButtonId();


        if (TextUtils.isEmpty(volunteer_address)) {

            address.setError("required field");
            showMessage("Address Must Not Be Empty");
            return;
        }

        if (TextUtils.isEmpty(volunteer_phone)) {

            phone.setError("required field");
            showMessage("Mobile Number Must Not Be Empty");
            return;
        }

        if (TextUtils.isEmpty(volunteer_communicate_time)) {

            communicateTime.setError("required field");
            showMessage("Communication Time Must Not Be Empty");
            return;
        }

        if (radio_id_contribution == -1) {

            showMessage("You Need To Select A Way To Contribute");
            return;
        }


        if (radio_id_time == -1) {

            showMessage("You Need To Select How Much Time You Can Contribute");
            return;
        }

        if (radio_id_contribution == R.id.others_contribute) {

            if (TextUtils.isEmpty(otherContribute.getText().toString())) {

                otherContribute.setError("required field");
                showMessage("You Need To Fill Way Of Contribute");
                return;
            }
        }

        if (radio_id_time == R.id.others_timing) {

            if (TextUtils.isEmpty(otherTiming.getText().toString())) {

                otherTiming.setError("required field");
                showMessage("You Need To Fill How Much Time You Can Contribute");
                return;
            }
        }

        if (!monday.isChecked() && !tuesday.isChecked() && !wednesday.isChecked() && !thursday.isChecked() && !friday.isChecked() && !saturday.isChecked() && !sunday.isChecked()) {

            showMessage("Please Check What Days Are Good For You");
            return;
        }

        final DatabaseReference volunteer_registration = FirebaseDatabase.getInstance().getReference("volunteer_registration").push();

        DatabaseReference unchecked_volunteer = FirebaseDatabase.getInstance().getReference("unchecked_volunteer").child(Objects.requireNonNull(volunteer_registration.getKey()));
        unchecked_volunteer.setValue(true);

        final Map<String, Object> map = new HashMap<>();

        map.put("mail_id", volunteer_mail);
        map.put("name", volunteer_name);
        map.put("phone", volunteer_phone);
        map.put("address", volunteer_address);
        map.put("work", false);
        map.put("id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        map.put("time_to_communicate", volunteer_communicate_time);
        if (!TextUtils.isEmpty(comment.getText().toString()))
            map.put("comment", volunteer_comment);
        if (wayOfContribution.getCheckedRadioButtonId() == R.id.others_contribute)
            map.put("way_of_contribution", otherContribute.getText().toString());
        else
            map.put("way_of_contribution", checkedRadioButtonMessageForWayOfContribution(wayOfContribution.getCheckedRadioButtonId()));

        if (timeToContribute.getCheckedRadioButtonId() == R.id.others_timing)
            map.put("time_to_contribute", otherContribute.getText().toString());
        else
            map.put("time_to_contribute", checkedRadioButtonMessageForTimeToContribute(wayOfContribution.getCheckedRadioButtonId()));

        map.put("submitted_time", ServerValue.TIMESTAMP);

        volunteer_registration.updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {

                    DatabaseReference days = FirebaseDatabase.getInstance().getReference("volunteer_registration").child(Objects.requireNonNull(volunteer_registration.getKey())).child("days");

                    Map<String, Object> day_map = new HashMap<>();

                    if (monday.isChecked())
                        day_map.put("monday", true);
                    else
                        day_map.put("monday", false);
                    if (tuesday.isChecked())
                        day_map.put("tuesday", true);
                    else
                        day_map.put("tuesday", false);
                    if (wednesday.isChecked())
                        day_map.put("wednesday", true);
                    else
                        day_map.put("wednesday", false);
                    if (thursday.isChecked())
                        day_map.put("thursday", true);
                    else
                        day_map.put("thursday", false);
                    if (friday.isChecked())
                        day_map.put("friday", true);
                    else
                        day_map.put("friday", false);
                    if (saturday.isChecked())
                        day_map.put("saturday", true);
                    else
                        day_map.put("saturday", false);

                    if (sunday.isChecked())
                        day_map.put("sunday", true);
                    else
                        day_map.put("sunday", false);

                    days.updateChildren(day_map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            String current_user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            DatabaseReference books_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("books");
                            books_ref.setValue("yes");

                            View dialogView = getLayoutInflater().inflate(R.layout.volunteer_registration_dialog, null);

                            Button ok = dialogView.findViewById(R.id.volunteer_ok_button);

                            final AlertDialog alertDialog = new AlertDialog.Builder(VolunteerRegistrationActivity.this)
                                    .setCancelable(false)
                                    .setView(dialogView)
                                    .create();
                            alertDialog.show();

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("mailto:prasanth_vinnakota@yahoo.com"));
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "volunteer registration");
                                    intent.putExtra(Intent.EXTRA_TEXT, "email : " + volunteer_mail +
                                            "\nname : " + volunteer_name +
                                            "\nphone : " + volunteer_phone +
                                            "\naddress : " + volunteer_address +
                                            "\nway of communication : " + volunteer_communicate_time +
                                            "\ncomments : " + volunteer_comment +
                                            "\nway of contribution : " + checkedRadioButtonMessageForWayOfContribution(wayOfContribution.getCheckedRadioButtonId()) +
                                            "\ntime to contribute : " + checkedRadioButtonMessageForTimeToContribute(timeToContribute.getCheckedRadioButtonId()) +
                                            "\nkey : " + volunteer_registration.getKey());

                                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                                    intent2.setData(Uri.parse("mailto:spiritualtablet@gmail.com"));
                                    intent2.putExtra(Intent.EXTRA_SUBJECT, "volunteer registration");
                                    intent2.putExtra(Intent.EXTRA_TEXT, "email : " + volunteer_mail +
                                            "\nname : " + volunteer_name +
                                            "\nphone : " + volunteer_phone +
                                            "\naddress : " + volunteer_address +
                                            "\nway of communication : " + volunteer_communicate_time +
                                            "\ncomments : " + volunteer_comment +
                                            "\nway of contribution : " + checkedRadioButtonMessageForWayOfContribution(wayOfContribution.getCheckedRadioButtonId()) +
                                            "\ntime to contribute : " + checkedRadioButtonMessageForTimeToContribute(timeToContribute.getCheckedRadioButtonId()) +
                                            "\nkey : " + volunteer_registration.getKey());

                                    Intent intent3 = new Intent(Intent.ACTION_VIEW);
                                    intent3.setData(Uri.parse("mailto:ramya.gunisetty@gmail.com"));
                                    intent3.putExtra(Intent.EXTRA_SUBJECT, "volunteer registration");
                                    intent3.putExtra(Intent.EXTRA_TEXT, "email : " + volunteer_mail +
                                            "\nname : " + volunteer_name +
                                            "\nphone : " + volunteer_phone +
                                            "\naddress : " + volunteer_address +
                                            "\nway of communication : " + volunteer_communicate_time +
                                            "\ncomments : " + volunteer_comment +
                                            "\nway of contribution : " + checkedRadioButtonMessageForWayOfContribution(wayOfContribution.getCheckedRadioButtonId()) +
                                            "\ntime to contribute : " + checkedRadioButtonMessageForTimeToContribute(timeToContribute.getCheckedRadioButtonId()) +
                                            "\nkey : " + volunteer_registration.getKey());


                                    alertDialog.dismiss();
                                    startActivities(new Intent[]{intent, intent2, intent3});
                                    //startActivity(new Intent(VolunteerRegistrationActivity.this, DashBoardActivity.class));
                                    finish();

                                }
                            });
                        }
                    });

                }
                else
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

    private String checkedRadioButtonMessageForWayOfContribution(int checkedRadioButtonId) {

        switch (checkedRadioButtonId) {
            case R.id.admin_works:
                return "Admin Works";

            case R.id.creating_flyers:
                return "Creating Flyers";

            case R.id.any_digital_work:
                return "Any Digital Work";

            case R.id.social_media:
                return "Social Media";

            case R.id.event_management:
                return "Event Management";

            case R.id.taking_meditation_classes:
                return "Taking Meditation Classes";

            case R.id.others_contribute:
                return otherContribute.getText().toString();
        }
        return "nothing selected";
    }

    private String checkedRadioButtonMessageForTimeToContribute(int checkedRadioButtonId) {

        switch (checkedRadioButtonId) {
            case R.id.one_hour_a_day:
                return "one hour a day";

            case R.id.one_hour_a_week:
                return "one hour a week";

            case R.id.one_hour_a_month:
                return "one hour a month";

            case R.id.ten_minutes_a_week:
                return "ten minutes a week";

            case R.id.others_timing:
                return otherTiming.getText().toString();
        }
        return "nothing selected";
    }

    private void showMessage(String message){

        Toast.makeText(VolunteerRegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}

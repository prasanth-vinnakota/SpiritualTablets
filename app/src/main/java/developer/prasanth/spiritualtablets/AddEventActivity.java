package developer.prasanth.spiritualtablets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {

    ImageView eventImage;
    TextInputEditText name, description, timing, link;
    DatabaseReference events_ref;
    Spinner languageSpinner;
    String selectedLanguage = "Select Language";
    Uri filePath;
    int REQUEST_CODE = 1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventImage = findViewById(R.id.add_events_image);

        progressDialog = new ProgressDialog(AddEventActivity.this);
        progressDialog.setTitle("Uploading");

        name = findViewById(R.id.add_events_name);
        description = findViewById(R.id.add_events_description);
        link = findViewById(R.id.add_events_link);
        timing = findViewById(R.id.add_events_timing);


        languageSpinner = findViewById(R.id.add_events_spinner);

        final String[] languages = {"Select Language", "English", "Telugu", "Hindi", "Others"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(languageAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                filePath = result.getUri();
                eventImage.setImageURI(filePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception exception = result.getError();
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addEvent(View view) {

        final String event_name, event_desc, event_timing, event_link;

        event_desc = Objects.requireNonNull(description.getText()).toString();
        event_name = Objects.requireNonNull(name.getText()).toString();
        event_link = Objects.requireNonNull(link.getText()).toString();
        event_timing = Objects.requireNonNull(timing.getText()).toString();


        if (TextUtils.isEmpty(event_name)) {
            Toast.makeText(this, "event name must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!selectedLanguage.equals("Select Language")) {

            events_ref = FirebaseDatabase.getInstance().getReference("events").child(selectedLanguage).child(event_name);

            events_ref.child("language").setValue(selectedLanguage);
            events_ref.child("name").setValue(event_name);

            if (filePath != null) {

                StorageReference event_image_reference = FirebaseStorage.getInstance().getReference().child("Event Pictures");
                final StorageReference eventImageFilePath = event_image_reference.child(event_name + ".jpg");
                eventImageFilePath.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        eventImageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                events_ref.child("image").setValue(uri.toString());
                            }
                        });
                    }
                });
            }

            if (!TextUtils.isEmpty(event_desc)) {

                events_ref.child("description").setValue(event_desc);
            }

            if (!TextUtils.isEmpty(event_timing)) {

                events_ref.child("timing").setValue(event_timing);
            }
            if (!TextUtils.isEmpty(event_link)) {

                events_ref.child("link").setValue(event_link);
            }

            eventImage.setImageURI(null);
            name.setText("");
            description.setText("");
            timing.setText("");
            link.setText("");

            Toast.makeText(getApplicationContext(), "Event Added Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddEventActivity.this, EventsByLanguageActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Select language", Toast.LENGTH_SHORT).show();
        }

    }
}

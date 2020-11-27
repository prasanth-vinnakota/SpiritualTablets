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
import com.google.android.gms.tasks.OnFailureListener;
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
    TextInputEditText name;
    TextInputEditText description;
    TextInputEditText timing;
    TextInputEditText link;
    DatabaseReference eventsReference;
    Spinner languageSpinner;
    String selectedLanguage = "Select Language";
    Uri filePath;
    int REQUEST_CODE = 1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        init();
    }

    private void init(){

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
                    .start(AddEventActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                if (result != null) {
                    filePath = result.getUri();
                }
                eventImage.setImageURI(filePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception exception = null;
                if (result != null) {
                    exception = result.getError();
                }
                if (exception != null) {
                    showMessage(exception.getMessage());
                }
            }
        }
    }

    public void addEvent(View view) {

        final String event_name;
        final String event_desc;
        final String event_timing;
        final String event_link;

        event_desc = Objects.requireNonNull(description.getText()).toString();
        event_name = Objects.requireNonNull(name.getText()).toString();
        event_link = Objects.requireNonNull(link.getText()).toString();
        event_timing = Objects.requireNonNull(timing.getText()).toString();


        if (TextUtils.isEmpty(event_name)) {

            showMessage("Event Name Must Not Be Empty");
            return;
        }

        if (!selectedLanguage.equals("Select Language")) {

            eventsReference = FirebaseDatabase.getInstance().getReference("events").child(selectedLanguage).child(event_name);

            eventsReference.child("language").setValue(selectedLanguage);
            eventsReference.child("name").setValue(event_name);

            if (filePath != null) {

                StorageReference event_image_reference = FirebaseStorage.getInstance().getReference().child("Event Pictures");
                final StorageReference eventImageFilePath = event_image_reference.child(event_name + ".jpg");

                eventImageFilePath.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        eventImageFilePath.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                eventsReference.child("image").setValue(uri.toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage(e.getMessage());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                    }
                });
            }

            if (!TextUtils.isEmpty(event_desc)) {

                eventsReference.child("description").setValue(event_desc);
            }

            if (!TextUtils.isEmpty(event_timing)) {

                eventsReference.child("timing").setValue(event_timing);
            }
            if (!TextUtils.isEmpty(event_link)) {

                eventsReference.child("link").setValue(event_link);
            }

            eventImage.setImageURI(null);
            name.setText("");
            description.setText("");
            timing.setText("");
            link.setText("");

            showMessage("Event Added Successfully");
            startActivity(new Intent(AddEventActivity.this, DashBoardActivity.class));
            finish();

        } else {

            showMessage("Please Select Language");
        }
    }

    private void showMessage(String message){
        Toast.makeText(AddEventActivity.this, message, Toast.LENGTH_LONG).show();
    }
}

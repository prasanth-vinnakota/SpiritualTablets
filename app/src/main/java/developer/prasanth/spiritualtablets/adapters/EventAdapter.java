package developer.prasanth.spiritualtablets.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import developer.prasanth.spiritualtablets.DashBoardActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.ViewEventActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList;
    private DatabaseReference userAdminReference;
    private View dialogView;
    private String language;


    public EventAdapter(Context context, List<String> stringList, String language) {

        this.context = context;
        this.stringList = stringList;
        this.language = language;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_event_view_layout, parent, false);
        dialogView = LayoutInflater.from(context).inflate(R.layout.mark_as_complete_event_dialog, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final DatabaseReference eventReference = FirebaseDatabase.getInstance().getReference("events").child(language).child(stringList.get(position));
        eventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                 if (snapshot.exists()){

                     if (snapshot.child("completed").exists() && Objects.requireNonNull(snapshot.child("completed").getValue()).toString().equalsIgnoreCase("true"))
                         holder.eventCardView.setCardBackgroundColor(Color.GREEN);
                     else
                         holder.eventCardView.setCardBackgroundColor(Color.RED);

                         holder.eventName.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());

                         if (snapshot.child("description").getValue() != null)

                             holder.eventDescription.setText(Objects.requireNonNull(snapshot.child("description").getValue()).toString());
                         else {

                             holder.eventDescription.setVisibility(View.GONE);
                             holder.eventDescriptionTitle.setVisibility(View.GONE);
                         }

                         if (snapshot.child("timing").getValue() != null)
                             holder.eventTiming.setText(Objects.requireNonNull(snapshot.child("timing").getValue()).toString());
                         else {
                             holder.eventTiming.setVisibility(View.GONE);
                             holder.eventTimingTitle.setVisibility(View.GONE);
                         }

                         if (snapshot.child("image").getValue() != null)
                             Glide.with(context).load(Objects.requireNonNull(snapshot.child("image").getValue()).toString()).into(holder.eventImage);
                         else holder.eventImage.setVisibility(View.GONE);

                         if (snapshot.child("link").getValue() != null){
                             holder.eventLink.setText(Objects.requireNonNull(snapshot.child("link").getValue()).toString());
                             holder.eventCardView.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     Intent intent = new Intent(Intent.ACTION_VIEW);
                                     intent.setData(Uri.parse(Objects.requireNonNull(snapshot.child("link").getValue()).toString()));
                                     context.startActivity(intent);
                                 }
                             });
                         } else {
                             holder.eventLink.setVisibility(View.GONE);
                             holder.eventLinkTitle.setVisibility(View.GONE);
                         }
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                showMessage(error.getMessage());
            }
        });

        holder.eventCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Button markAsCompleteButton = dialogView.findViewById(R.id.mark_as_complete_event_dialog_mark_as_complete_button);
                Button cancelButton = dialogView.findViewById(R.id.mark_as_complete_event_dialog_cancel_button);

                if (dialogView.getParent() != null)
                    ((ViewGroup)dialogView.getParent()).removeView(dialogView);

                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                alertDialog.show();

                markAsCompleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        userAdminReference = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("admin");

                        userAdminReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    eventReference.child("completed").setValue(true)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                eventReference.child("link").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()) {
                                                                    showMessage("Event Marked As Completed");
                                                                    context.startActivity(new Intent(context, DashBoardActivity.class));
                                                                }
                                                                else
                                                                    showMessage(Objects.requireNonNull(task.getException()).getMessage());
                                                            }
                                                        });
                                            } else
                                                showMessage(Objects.requireNonNull(task.getException()).getMessage());
                                            alertDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessage(e.getMessage());
                                        }
                                    });
                                } else {

                                    showMessage("You Don't Have Permission To Complete The Event");
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                showMessage(error.getMessage());
                            }
                        });

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView eventDescription;
        TextView eventTiming;
        TextView eventLink;
        TextView eventDescriptionTitle;
        TextView eventTimingTitle;
        TextView eventLinkTitle;
        CardView eventCardView;
        ImageView eventImage;
        MyViewHolder(@NonNull View itemView) {

            super(itemView);
            eventCardView = itemView.findViewById(R.id.single_event_view_layout_card_view);
            eventName = itemView.findViewById(R.id.single_event_view_layout_event_name);
            eventDescription = itemView.findViewById(R.id.single_event_view_layout_event_description);
            eventDescriptionTitle = itemView.findViewById(R.id.single_event_view_layout_event_description_title);
            eventTiming = itemView.findViewById(R.id.single_event_view_layout_event_timing);
            eventTimingTitle = itemView.findViewById(R.id.single_event_view_layout_event_timing_title);
            eventLink = itemView.findViewById(R.id.single_event_view_layout_event_link);
            eventLinkTitle = itemView.findViewById(R.id.single_event_view_layout_event_link_title);
            eventImage = itemView.findViewById(R.id.single_event_view_layout_event_image);
        }
    }

    private void showMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();

    }
}

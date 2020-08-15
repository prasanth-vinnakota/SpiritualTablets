package com.example.spiritualtablets.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.spiritualtablets.R;
import com.example.spiritualtablets.models.EventItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context applicationContext;
    private List<EventItem> eventItemList;
    private DatabaseReference user_admin_ref, event_ref;
    private View dialog_view;
    private StorageReference event_image_ref;


    public EventAdapter(Context applicationContext, List<EventItem> eventItems) {

        this.applicationContext = applicationContext;
        eventItemList = eventItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(applicationContext).inflate(R.layout.row_event_items, parent, false);
        dialog_view  = LayoutInflater.from(applicationContext).inflate(R.layout.delete_event_dialog, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (eventItemList.get(position).getLink() != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(eventItemList.get(position).getLink()));
                    applicationContext.startActivity(intent);
                }
            });

        }



        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Button delete = dialog_view.findViewById(R.id.alert_delete_event_button);
                Button cancel = dialog_view.findViewById(R.id.alert_delete_event_cancel);

                final AlertDialog alertDialog = new AlertDialog.Builder(applicationContext)
                        .setView(dialog_view)
                        .setCancelable(false)
                        .create();

                alertDialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        user_admin_ref = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("admin");

                        user_admin_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){


                                    event_ref = FirebaseDatabase.getInstance().getReference("events").child(eventItemList.get(position).getLanguage()).child(eventItemList.get(position).getName());

                                    event_ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            event_image_ref = FirebaseStorage.getInstance().getReference().child("Event Pictures").child(eventItemList.get(position).getName() + ".jpg");

                                            event_image_ref.delete();

                                            Toast.makeText(applicationContext, "Event Deleted", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });



                                }
                                else {
                                    Toast.makeText(holder.itemView.getContext(), "You don't have permission to delete", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return false;
            }
        });

        if (eventItemList.get(position).getName() != null){
            holder.eventName.setText(eventItemList.get(position).getName());
        }
        else {
            holder.eventName.setVisibility(View.GONE);
            holder.eventNameTv.setVisibility(View.GONE);
        }

        if (eventItemList.get(position).getDescription() != null){
            holder.eventDescription.setText(eventItemList.get(position).getDescription());

        }
        else {
            holder.eventDescription.setVisibility(View.GONE);
            holder.eventDescriptionTv.setVisibility(View.GONE);
        }

        if (eventItemList.get(position).getTiming() != null){
            holder.eventTiming.setText(eventItemList.get(position).getTiming());
        }
        else {
            holder.eventTiming.setVisibility(View.GONE);
            holder.eventTimingTv.setVisibility(View.GONE);
        }

        if (eventItemList.get(position).getImage() != null){
            Glide.with(applicationContext).load(eventItemList.get(position).getImage()).into(holder.eventImage);
        }else {
            holder.eventImage.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return eventItemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView eventName, eventNameTv, eventDescription, eventDescriptionTv, eventTiming, eventTimingTv;
        ImageView eventImage;
        CardView cardView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventNameTv = itemView.findViewById(R.id.event_name_tv);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDescriptionTv = itemView.findViewById(R.id.event_description_tv);
            eventTiming = itemView.findViewById(R.id.event_timing);
            eventTimingTv = itemView.findViewById(R.id.event_timing_tv);

            eventImage = itemView.findViewById(R.id.events_image);

            cardView = itemView.findViewById(R.id.event_item_card_view);

        }
    }
}

package com.example.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spiritualtablets.R;
import com.example.spiritualtablets.models.EventItem;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context applicationContext;
    private List<EventItem> eventItemList;


    public EventAdapter(Context applicationContext, List<EventItem> eventItems) {

        this.applicationContext = applicationContext;
        eventItemList = eventItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(applicationContext).inflate(R.layout.row_event_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (eventItemList.get(position).getLink() != null)
        {holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(eventItemList.get(position).getLink()));
                applicationContext.startActivity(intent);
            }
        });

        }

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
        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventNameTv = itemView.findViewById(R.id.event_name_tv);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDescriptionTv = itemView.findViewById(R.id.event_description_tv);
            eventTiming = itemView.findViewById(R.id.event_timing);
            eventTimingTv = itemView.findViewById(R.id.event_timing_tv);

            eventImage = itemView.findViewById(R.id.events_image);

        }
    }
}

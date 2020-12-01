package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.VideoPlayerActivity;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.MyViewHolder> {

    List<String> stringList;
    Context context;
    String language;

    public YoutubeAdapter(String language, List<String> stringList, Context context) {
        this.language = language;
        this.stringList = stringList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_video_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.videoName.setText(stringList.get(position));

        holder.videoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("language", language);
                intent.putExtra("name",stringList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView videoName;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            videoName = itemView.findViewById(R.id.single_video_view_layout_video_name);
        }
    }
}

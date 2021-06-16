package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.prasanth.spiritualtablets.MusicPlayerActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.ItemBean;

public class MusicMeditationAdapter extends RecyclerView.Adapter<MusicMeditationAdapter.MyViewHolder> {

    private final Context context;
    private final List<ItemBean> itemBeans;
    private final String time;

    public MusicMeditationAdapter(Context context, List<ItemBean> itemBeans, String time) {
        this.context = context;
        this.itemBeans = itemBeans;
        this.time = time;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_audio_view_layout,parent, false);
        return new MusicMeditationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.musicName.setText(itemBeans.get(position).getName());
        holder.musicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Loading "+itemBeans.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("name",itemBeans.get(position).getName());
                intent.putExtra("link",itemBeans.get(position).getLink());
                intent.putExtra("time",time);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemBeans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView musicName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            musicName = itemView.findViewById(R.id.single_audio_view_layout_audio_name);
        }
    }
}

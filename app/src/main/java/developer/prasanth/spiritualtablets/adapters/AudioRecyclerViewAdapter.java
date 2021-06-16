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

import developer.prasanth.spiritualtablets.AudioPlayerActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.ItemBean;
import java.util.List;

public class AudioRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecyclerViewAdapter.MyViewHolder>{


    private final Context context;
    private final List<ItemBean> itemBeans;
    private final String language;

    public AudioRecyclerViewAdapter(Context context, List<ItemBean> audioBeans,String language) {
        this.context = context;
        this.itemBeans = audioBeans;
        this.language = language;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_audio_view_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.audioName.setText(itemBeans.get(position).getName());
        holder.audioName.setOnClickListener(v -> {

            showMessage("Loading "+itemBeans.get(position).getName());
            Intent intent = new Intent(context, AudioPlayerActivity.class);
            intent.putExtra("url", itemBeans.get(position).getLink());
            intent.putExtra("audio_name", itemBeans.get(position).getName());
            intent.putExtra("language",language);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemBeans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            audioName = itemView.findViewById(R.id.single_audio_view_layout_audio_name);

        }
    }

    private void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import developer.prasanth.spiritualtablets.AudioPlayerActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.DataItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AudioRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<DataItem> dataItems;

    public AudioRecyclerViewAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cardview_item_book,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_book_title.setText(dataItems.get(position).getTitle());
        holder.img_book_thumbnail.setImageResource(dataItems.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("audios").child(dataItems.get(position).getTitle());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            showMessage("Loading "+dataItems.get(position).getTitle());
                            String url = dataSnapshot.getValue(String.class);
                            Intent intent = new Intent(context, AudioPlayerActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("song_name", dataItems.get(position).getTitle());
                            intent.putExtra("image", dataItems.get(position).getThumbnail());
                            context.startActivity(intent);
                        }
                        else
                            showMessage("Audio Book Not Found");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        showMessage(databaseError.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_book_title;
        ImageView img_book_thumbnail;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_book_title = itemView.findViewById(R.id.book_title_id);
            img_book_thumbnail = itemView.findViewById(R.id.book_image_id);
            cardView = itemView.findViewById(R.id.cardview_id);

        }
    }

    private void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

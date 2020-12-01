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

        View view = LayoutInflater.from(context).inflate(R.layout.single_book_view_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.bookTitle.setText(dataItems.get(position).getTitle());
        holder.bookImage.setImageResource(dataItems.get(position).getThumbnail());
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
        TextView bookTitle;
        ImageView bookImage;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookTitle = itemView.findViewById(R.id.single_book_view_layout_book_title);
            bookImage = itemView.findViewById(R.id.single_book_view_layout_book_image);
            cardView = itemView.findViewById(R.id.single_book_view_layout_card_view);

        }
    }

    private void showMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

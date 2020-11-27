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


import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.ViewBookActivity;
import developer.prasanth.spiritualtablets.models.DataItem;

import java.util.List;

public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<DataItem> dataItems;

    public BooksRecyclerViewAdapter(Context context, List<DataItem> dataItems) {
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.tv_book_title.setText(dataItems.get(position).getTitle());
        holder.img_book_thumbnail.setImageResource(dataItems.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("language",dataItems.get(position).getCategory());
                intent.putExtra("book_name",dataItems.get(position).getTitle());
                context.startActivity(intent);
                showMessage("Loading " + dataItems.get(position).getTitle());
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

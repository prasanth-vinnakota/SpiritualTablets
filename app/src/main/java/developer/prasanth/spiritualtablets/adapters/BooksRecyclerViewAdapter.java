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

        View view = LayoutInflater.from(context).inflate(R.layout.single_book_view_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.bookTitle.setText(dataItems.get(position).getTitle());
        holder.bookImage.setImageResource(dataItems.get(position).getThumbnail());
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

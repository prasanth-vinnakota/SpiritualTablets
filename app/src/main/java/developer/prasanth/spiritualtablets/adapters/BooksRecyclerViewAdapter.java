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


import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.ViewBookActivity;
import developer.prasanth.spiritualtablets.models.ItemBean;

import java.util.List;

public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final List<ItemBean> itemBeans;
    private final String language;

    public BooksRecyclerViewAdapter(Context context, List<ItemBean> itemBeans,String language) {
        this.context = context;
        this.itemBeans = itemBeans;
        this.language = language;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_book_view_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.bookName.setText(itemBeans.get(position).getName());
        holder.bookName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Loading "+itemBeans.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra("url", itemBeans.get(position).getLink());
                intent.putExtra("book_name", itemBeans.get(position).getName());
                intent.putExtra("language",language);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemBeans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bookName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.single_book_view_layout_book_name);
        }
    }
}

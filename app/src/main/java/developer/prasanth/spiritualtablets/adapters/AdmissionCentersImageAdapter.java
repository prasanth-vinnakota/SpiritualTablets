package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import developer.prasanth.spiritualtablets.FullScreenImageActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.ItemBean;

public class AdmissionCentersImageAdapter extends RecyclerView.Adapter<AdmissionCentersImageAdapter.MyViewHolder>{

    Context context;
    List<ItemBean> itemBeans;

    public AdmissionCentersImageAdapter(Context context, List<ItemBean> itemBeans) {
        this.context = context;
        this.itemBeans = itemBeans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gallery_image_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Glide.with(context).load(itemBeans.get(position).getLink()).into(holder.imageView);
        Picasso.get().load(itemBeans.get(position).getLink()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                intent.setData(Uri.parse(itemBeans.get(position).getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemBeans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.single_gallery_image_view_layout_image_view);
        }
    }
}

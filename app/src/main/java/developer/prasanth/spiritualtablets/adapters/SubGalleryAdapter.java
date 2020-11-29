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

import java.util.List;

import developer.prasanth.spiritualtablets.FullScreenImageActivity;
import developer.prasanth.spiritualtablets.R;

public class SubGalleryAdapter extends RecyclerView.Adapter<SubGalleryAdapter.SubGalleryViewHolder> {

    Context context;
    List<String> stringList;

    public SubGalleryAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public SubGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_gallery_image_view_layout,parent,false);
        return new SubGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubGalleryViewHolder holder, final int position) {

        Glide.with(context).load(stringList.get(position)).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                intent.setData(Uri.parse(stringList.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class SubGalleryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public SubGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.single_gallery_image_view_layout_image_view);
        }
    }
}

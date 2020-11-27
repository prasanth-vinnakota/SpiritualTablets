package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import developer.prasanth.spiritualtablets.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context context;
    private List<String> stringList;

    public GalleryAdapter(Context context, List<String> stringList) {

        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.image_view,parent,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

        Glide.with(context).load(stringList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public GalleryViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.gallery_image);
        }
    }
}

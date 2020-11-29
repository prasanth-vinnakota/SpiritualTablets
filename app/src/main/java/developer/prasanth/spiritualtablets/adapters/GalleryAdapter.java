package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        View view = LayoutInflater.from(context).inflate(R.layout.single_gallery_view_layout,parent,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {

        holder.galleryName.setText(stringList.get(position));

        DatabaseReference imagesReference = FirebaseDatabase.getInstance().getReference("gallery").child(stringList.get(position));
        imagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<String> list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        list.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    }
                    SubGalleryAdapter subGalleryAdapter = new SubGalleryAdapter(context,list);
                    holder.galleryRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
                    holder.galleryRecyclerView.setAdapter(subGalleryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder{

        TextView galleryName;
        RecyclerView galleryRecyclerView;
        public GalleryViewHolder(@NonNull View itemView) {

            super(itemView);
            galleryName = itemView.findViewById(R.id.single_gallery_view_layout_gallery_name);
            galleryRecyclerView = itemView.findViewById(R.id.single_gallery_view_layout_recycler_view);
        }
    }
}

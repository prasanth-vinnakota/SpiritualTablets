package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        DatabaseReference gallery_ref = FirebaseDatabase.getInstance().getReference("Gallery").child("Users").child(current_user_id);
        gallery_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check){
                    long count = snapshot.getChildrenCount();
                    count++;
                    gallery_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                }
                check = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference("users").child(current_user_id).child("Gallery");
        user_ref.addValueEventListener(new ValueEventListener() {
            boolean check = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (check){
                    long count = snapshot.getChildrenCount();
                    count++;
                    user_ref.child(String.valueOf(count)).setValue(getDateAndTime());
                }
                check = false;
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

    private String getDateAndTime() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(new Date().getTime());
        return DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar).toString();
    }
}

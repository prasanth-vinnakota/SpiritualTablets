package developer.prasanth.spiritualtablets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import developer.prasanth.spiritualtablets.models.Contacts;

public class GetUsersThroughUserId extends RecyclerView.Adapter<GetUsersThroughUserId.GetUsersThroughUserIdViewHolder> implements Filterable {

    List<String> userIds;
    List<Contacts> contactsList = new ArrayList<>();
    List<String> userIdFull;

    Context context;

    public GetUsersThroughUserId(List<String> userIds, Context context) {
        this.userIds = userIds;
        this.context = context;
        userIdFull = new ArrayList<>(userIds);
    }

    @NonNull
    @Override
    public GetUsersThroughUserIdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_display_layout,parent,false);
        return new GetUsersThroughUserIdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GetUsersThroughUserIdViewHolder holder, int position) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userIds.get(position));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    final Contacts contacts = snapshot.getValue(Contacts.class);
                    assert contacts != null;
                    contacts.setUserId(snapshot.getKey());
                    contactsList.add(contacts);
                    holder.userName.setText(contacts.getFull_name());
                    holder.userEmail.setText(contacts.getProfile_status());
                    if (contacts.getImage() != null)
                        Picasso.get().load(contacts.getImage()).into(holder.userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userIds.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(userIdFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Contacts contacts : contactsList) {
                    if (contacts.getFull_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contacts.getUserId());
                        continue;
                    }
                    if (contacts.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contacts.getUserId());
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            userIds.clear();
            contactsList.clear();
            userIds.addAll((List<String>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    static class GetUsersThroughUserIdViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userProfileImage;
        TextView userName, userEmail;
        ImageView userOnlineStatus;

        public GetUsersThroughUserIdViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.user_display_layout_user_image);
            userEmail = itemView.findViewById(R.id.user_display_layout_user_email);
            userName = itemView.findViewById(R.id.user_display_layout_user_name);

            userOnlineStatus = itemView.findViewById(R.id.user_display_layout_user_online);
        }
    }
}

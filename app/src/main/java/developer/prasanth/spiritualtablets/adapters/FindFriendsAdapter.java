package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import developer.prasanth.spiritualtablets.ProfileActivity;
import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.Contacts;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Contacts> list;
    private List<Contacts> listFull;

    public FindFriendsAdapter(Context context, List<Contacts> list) {
        this.context = context;
        this.list = list;
        listFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_display_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.userName.setText(list.get(position).getFull_name());
        String email = "@" + list.get(position).getEmail();
        holder.userEmail.setText(email);
        Picasso.get().load(list.get(position).getImage()).placeholder(R.drawable.user_photo).into(holder.userProfileImage);
        //Glide.with(context).load(list.get(position).getImage()).into(holder.userProfileImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", list.get(position).getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userProfileImage;
        TextView userName, userEmail;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.user_display_layout_user_image);
            userEmail = itemView.findViewById(R.id.user_display_layout_user_email);
            userName = itemView.findViewById(R.id.user_display_layout_user_name);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Contacts> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Contacts contacts : listFull) {
                    if (contacts.getFull_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contacts);
                        continue;
                    }
                    if (contacts.getEmail().toLowerCase().contains(filterPattern)) {
                        filteredList.add(contacts);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            list.clear();
            list.addAll((List<Contacts>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

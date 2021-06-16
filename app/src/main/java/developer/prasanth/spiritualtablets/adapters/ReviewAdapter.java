package developer.prasanth.spiritualtablets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.ReviewBean;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{

    Context context;
    List<ReviewBean> reviewBeans;

    public ReviewAdapter(Context context, List<ReviewBean> reviewBeans) {
        this.context = context;
        this.reviewBeans = reviewBeans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.single_review_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(reviewBeans.get(position).getName());
        holder.review.setText(reviewBeans.get(position).getComments());
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating(Integer.parseInt(reviewBeans.get(position).getStars()));


    }

    @Override
    public int getItemCount() {
        return reviewBeans.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, review;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_review_view_layout_name);
            review = itemView.findViewById(R.id.single_review_view_layout_review);
            ratingBar = itemView.findViewById(R.id.single_review_view_layout_rating_bar);
        }
    }
}

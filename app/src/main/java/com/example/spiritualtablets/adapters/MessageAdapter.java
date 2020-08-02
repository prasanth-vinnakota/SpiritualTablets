package com.example.spiritualtablets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spiritualtablets.R;
import com.example.spiritualtablets.models.MessageItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context mContext;
    private List<MessageItem> mList;

    public MessageAdapter(Context mContext, List<MessageItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.message_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!mList.get(position).getUserId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {


            SpannableString spannableString = new SpannableString(mList.get(position).getName());

            Random random = new Random();
            int random_number = random.nextInt(7);

            switch (random_number){
                case 0:
                    ForegroundColorSpan colorSpan0 = new ForegroundColorSpan(Color.DKGRAY);
                    spannableString.setSpan(colorSpan0, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 1:
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.GRAY);
                    spannableString.setSpan(colorSpan1, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 2:
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.RED);
                    spannableString.setSpan(colorSpan2, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 3:
                    ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.GREEN);
                    spannableString.setSpan(colorSpan3, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 4:
                    ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.BLUE);
                    spannableString.setSpan(colorSpan4, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 5:
                    ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.YELLOW);
                    spannableString.setSpan(colorSpan5, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 6:
                    ForegroundColorSpan colorSpan6 = new ForegroundColorSpan(Color.CYAN);
                    spannableString.setSpan(colorSpan6, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                default:
                    ForegroundColorSpan colorSpanDefault = new ForegroundColorSpan(Color.MAGENTA);
                    spannableString.setSpan(colorSpanDefault, 0, mList.get(position).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

            }
            holder.userName.setText(spannableString);
        }
        else {
            SpannableString spannableString = new SpannableString("You");
            Random random = new Random();
            int random_number = random.nextInt(7);

            switch (random_number){
                case 0:
                    ForegroundColorSpan colorSpan0 = new ForegroundColorSpan(Color.DKGRAY);
                    spannableString.setSpan(colorSpan0, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 1:
                    ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.GRAY);
                    spannableString.setSpan(colorSpan1, 0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 2:
                    ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.RED);
                    spannableString.setSpan(colorSpan2, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 3:
                    ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.GREEN);
                    spannableString.setSpan(colorSpan3, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 4:
                    ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.BLUE);
                    spannableString.setSpan(colorSpan4, 0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 5:
                    ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.YELLOW);
                    spannableString.setSpan(colorSpan5, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                case 6:
                    ForegroundColorSpan colorSpan6 = new ForegroundColorSpan(Color.CYAN);
                    spannableString.setSpan(colorSpan6, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

                default:
                    ForegroundColorSpan colorSpanDefault = new ForegroundColorSpan(Color.MAGENTA);
                    spannableString.setSpan(colorSpanDefault, 0,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;

            }
            holder.userName.setText(spannableString);

        }

        holder.userMessage.setText(mList.get(position).getMessage());

        long time = (long) mList.get(position).getTimeStamp();
        SpannableString spannableDate = new SpannableString(timestampToString(time));
        ForegroundColorSpan light_grey = new ForegroundColorSpan(Color.LTGRAY);
        spannableDate.setSpan(light_grey, 0, timestampToString(time).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.userDate.setText(spannableDate);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName, userMessage, userDate;
        CardView cardView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.group_chat_user_name);
            userMessage = itemView.findViewById(R.id.group_chat_user_message);
            userDate = itemView.findViewById(R.id.group_chat_user_date);

            cardView = itemView.findViewById(R.id.group_chat_card_view);

        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();


    }
}

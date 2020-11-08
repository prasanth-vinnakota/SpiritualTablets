package developer.prasanth.spiritualtablets.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.models.Messages;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessagesAdapter(List<Messages> userMessagesList){

        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String messageSenderId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String messageType = messages.getType();

        if (messageType.equals("text")){

            holder.receiverCardView.setVisibility(View.INVISIBLE);
            holder.senderCardView.setVisibility(View.INVISIBLE);

            if (fromUserId.equals(messageSenderId)){
                holder.senderCardView.setVisibility(View.VISIBLE);
                holder.senderMessageText.setText(messages.getMessage());
                long time = (long) messages.getTimestamp();
                holder.senderTime.setText(timestampToString(time));
            }else{
                holder.receiverCardView.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setText(messages.getMessage());
                long time = (long) messages.getTimestamp();
                holder.receiverTime.setText(timestampToString(time));
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView senderMessageText, receiverMessageText, senderTime, receiverTime;
        public CardView senderCardView, receiverCardView;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            senderCardView = itemView.findViewById(R.id.sender_card_view);
            receiverCardView = itemView.findViewById(R.id.receiver_card_view);
            receiverTime = itemView.findViewById(R.id.receiver_message_time);
            senderTime = itemView.findViewById(R.id.sender_message_time);
        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();


    }
}

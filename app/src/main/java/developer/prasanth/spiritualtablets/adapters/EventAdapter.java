package developer.prasanth.spiritualtablets.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import developer.prasanth.spiritualtablets.R;
import developer.prasanth.spiritualtablets.ViewEventActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private Context context;
    private List<String> stringList;
    private DatabaseReference userAdminReference, eventReference;
    private View dialogView;
    private StorageReference eventImageReference;
    private String language;


    public EventAdapter(Context context, List<String> stringList, String language) {

        this.context = context;
        this.stringList = stringList;
        this.language = language;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_item_view, parent, false);
        dialogView = LayoutInflater.from(context).inflate(R.layout.delete_event_dialog, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.name.setText(stringList.get(position));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewEventActivity.class);
                intent.putExtra("language", language);
                intent.putExtra("name", stringList.get(position));
                context.startActivity(intent);
            }
        });

        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Button delete = dialogView.findViewById(R.id.alert_delete_event_button);
                Button cancel = dialogView.findViewById(R.id.alert_delete_event_cancel);

                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                alertDialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        userAdminReference = FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("admin");

                        userAdminReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    eventReference = FirebaseDatabase.getInstance().getReference("events").child(language).child(stringList.get(position));
                                    eventReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            eventImageReference = FirebaseStorage.getInstance().getReference().child("Event Pictures").child(stringList.get(position) + ".jpg");
                                            eventImageReference.delete();
                                            showMessage("Event Deleted");
                                            alertDialog.dismiss();
                                        }
                                    });
                                } else {

                                    showMessage("You Don't Have Permission To Delete");
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                showMessage(error.getMessage());
                            }
                        });

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        MyViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.youtube_list_text_view);
        }
    }

    private void showMessage(String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

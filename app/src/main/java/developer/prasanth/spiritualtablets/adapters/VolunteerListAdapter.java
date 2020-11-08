package developer.prasanth.spiritualtablets.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import developer.prasanth.spiritualtablets.R;

public class VolunteerListAdapter extends RecyclerView.Adapter<VolunteerListAdapter.VolunteerListViewHolder> {
    private ArrayList<String> volunteerList;
    private Context context;
    private View dialog_view;

    public VolunteerListAdapter(ArrayList<String> volunteerList, Context context) {
        this.volunteerList = volunteerList;
        this.context = context;
    }

    @NonNull
    @Override
    public VolunteerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_volunteer_view_layout, parent, false);
        dialog_view = LayoutInflater.from(context).inflate(R.layout.check_uncheck_volunteer, parent, false);
        return new VolunteerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VolunteerListViewHolder holder, final int position) {

        final DatabaseReference volunteer_ref = FirebaseDatabase.getInstance().getReference("volunteer_registration").child(volunteerList.get(position));
        volunteer_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("address").getValue() != null)
                        holder.address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());

                    if (snapshot.child("comment").getValue() != null)
                        holder.comments.setText(Objects.requireNonNull(snapshot.child("comment").getValue()).toString());

                    if (snapshot.child("days").getValue() != null) {
                        String day = "";
                        if (Objects.requireNonNull(snapshot.child("days").child("monday").getValue()).toString().equals("true"))
                            day += "monday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("tuesday").getValue()).toString().equals("true"))
                            day += "tuesday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("wednesday").getValue()).toString().equals("true"))
                            day += "wednesday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("thursday").getValue()).toString().equals("true"))
                            day += "thursday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("friday").getValue()).toString().equals("true"))
                            day += "friday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("saturday").getValue()).toString().equals("true"))
                            day += "saturday,";
                        if (Objects.requireNonNull(snapshot.child("days").child("sunday").getValue()).toString().equals("true"))
                            day += "sunday,";
                        holder.days.setText(day);
                    }

                    if (snapshot.child("mail_id").getValue() != null)
                        holder.email.setText(Objects.requireNonNull(snapshot.child("mail_id").getValue()).toString());

                    if (snapshot.child("name").getValue() != null)
                        holder.name.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());

                    if (snapshot.child("phone").getValue() != null)
                        holder.mobile.setText(Objects.requireNonNull(snapshot.child("phone").getValue()).toString());

                    if (snapshot.child("time_to_communicate").getValue() != null)
                        holder.time_to_communicate.setText(Objects.requireNonNull(snapshot.child("time_to_communicate").getValue()).toString());

                    if (snapshot.child("time_to_contribute").getValue() != null)
                        holder.time_to_contribute.setText(Objects.requireNonNull(snapshot.child("time_to_contribute").getValue()).toString());

                    if (snapshot.child("way_of_contribution").getValue() != null)
                        holder.way_of_contribution.setText(Objects.requireNonNull(snapshot.child("way_of_contribution").getValue()).toString());

                    if (snapshot.child("work").getValue() != null)
                        holder.work_assigned.setText(Objects.requireNonNull(snapshot.child("work").getValue()).toString());

                    holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {

                            Button check = dialog_view.findViewById(R.id.check_volunteer);
                            Button uncheck = dialog_view.findViewById(R.id.uncheck_volunteer);
                            Button cancel = dialog_view.findViewById(R.id.check_uncheck_cancel);

                            final AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setView(dialog_view)
                                    .setCancelable(false)
                                    .create();

                            dialog.show();

                            check.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final EditText editText = new EditText(context);
                                    editText.setHint("Work Assigned");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Work Assigned To Volunteer");
                                    builder.setView(editText);
                                    builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
                                            db_ref.child("checked_volunteer").child(volunteerList.get(position)).setValue(true);
                                            db_ref.child("unchecked_volunteer").child(volunteerList.get(position)).removeValue();
                                            db_ref.child("volunteer_registration").child(volunteerList.get(position)).child("work").setValue(editText.getText().toString());
                                            if (snapshot.child("id").getValue() != null){
                                                DatabaseReference work_ref =FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(snapshot.child("id").getValue()).toString()).child("work");
                                                work_ref.setValue(editText.getText().toString());
                                            }
                                            Toast.makeText(context, "Checked Successfully", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog1 = builder.create();
                                    dialog1.show();
                                }
                            });


                            uncheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
                                    db_ref.child("unchecked_volunteer").child(volunteerList.get(position)).setValue(true);
                                    db_ref.child("checked_volunteer").child(volunteerList.get(position)).removeValue();
                                    db_ref.child("volunteer_registration").child(volunteerList.get(position)).child("work").setValue(false);
                                    if (snapshot.child("id").getValue() != null){
                                        DatabaseReference work_ref =FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(snapshot.child("id").getValue()).toString()).child("work");
                                        work_ref.setValue(false);
                                    }
                                    Toast.makeText(context, "Unchecked Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }

    public static class VolunteerListViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, mobile, address, way_of_contribution, time_to_contribute, time_to_communicate, days, comments, work_assigned;
        CardView cardView;

        public VolunteerListViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.single_volunteer_view_card_view);
            name = itemView.findViewById(R.id.single_volunteer_name);
            email = itemView.findViewById(R.id.single_volunteer_email);
            mobile = itemView.findViewById(R.id.single_volunteer_mobile_no);
            address = itemView.findViewById(R.id.single_volunteer_address);
            way_of_contribution = itemView.findViewById(R.id.single_volunteer_way_of_contribution);
            time_to_contribute = itemView.findViewById(R.id.single_volunteer_time_to_contribute);
            time_to_communicate = itemView.findViewById(R.id.single_volunteer_time_to_communicate);
            days = itemView.findViewById(R.id.single_volunteer_days);
            comments = itemView.findViewById(R.id.single_volunteer_comments);
            work_assigned = itemView.findViewById(R.id.single_volunteer_work_assigned);
        }
    }
}

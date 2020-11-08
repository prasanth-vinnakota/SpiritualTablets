package developer.prasanth.spiritualtablets.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import developer.prasanth.spiritualtablets.R;

public class RapidRegistrationAdapter extends RecyclerView.Adapter<RapidRegistrationAdapter.RapidRegistrationViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;
    private View dialog_view;

    public RapidRegistrationAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RapidRegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_rapid_registration_view_dialog,parent,false);
        dialog_view = LayoutInflater.from(context).inflate(R.layout.check_uncheck_volunteer,parent,false);

        return new RapidRegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RapidRegistrationViewHolder holder, final int position) {

        DatabaseReference rapid_registered_ref = FirebaseDatabase.getInstance().getReference("rapid_registration").child(arrayList.get(position));
        rapid_registered_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    if (snapshot.child("first_name").getValue() != null)
                        holder.firstName.setText(Objects.requireNonNull(snapshot.child("first_name").getValue()).toString());

                    if (snapshot.child("last_name").getValue() != null)
                        holder.lastName.setText(Objects.requireNonNull(snapshot.child("last_name").getValue()).toString());

                    if (snapshot.child("age").getValue() != null)
                        holder.age.setText(Objects.requireNonNull(snapshot.child("age").getValue()).toString());

                    if (snapshot.child("gender").getValue() != null)
                        holder.gender.setText(Objects.requireNonNull(snapshot.child("gender").getValue()).toString());

                    if (snapshot.child("mobile").getValue() != null)
                        holder.mobile.setText(Objects.requireNonNull(snapshot.child("mobile").getValue()).toString());

                    if (snapshot.child("address").getValue() != null)
                        holder.address.setText(Objects.requireNonNull(snapshot.child("address").getValue()).toString());

                    if (snapshot.child("education").getValue() != null)
                        holder.education.setText(Objects.requireNonNull(snapshot.child("education").getValue()).toString());

                    if (snapshot.child("disease").getValue() != null)
                        holder.disease.setText(Objects.requireNonNull(snapshot.child("disease").getValue()).toString());

                    if (snapshot.child("mother_tongue").getValue() != null)
                        holder.motherTongue.setText(Objects.requireNonNull(snapshot.child("mother_tongue").getValue()).toString());

                    if (snapshot.child("email").getValue() != null)
                        holder.email.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());

                    if (snapshot.child("referred_by_name").getValue() != null)
                        holder.referredByName.setText(Objects.requireNonNull(snapshot.child("referred_by_name").getValue()).toString());

                    if (snapshot.child("referred_by_mobile").getValue() != null)
                        holder.referredByMobile.setText(Objects.requireNonNull(snapshot.child("referred_by_mobile").getValue()).toString());

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
                                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
                                    db_ref.child("checked_rapid_registration").child(arrayList.get(position)).setValue(true);
                                    db_ref.child("unchecked_rapid_registration").child(arrayList.get(position)).removeValue();
                                    Toast.makeText(context, "Checked Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                            uncheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
                                    db_ref.child("unchecked_rapid_registration").child(arrayList.get(position)).setValue(true);
                                    db_ref.child("checked_rapid_registration").child(arrayList.get(position)).removeValue();
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
        return arrayList.size();
    }

    public static class RapidRegistrationViewHolder extends RecyclerView.ViewHolder{
        TextView firstName, lastName, age, mobile, email, address, education, disease, motherTongue, referredByName, referredByMobile, gender;
        CardView cardView;

        public RapidRegistrationViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.rapid_registration_view_first_name);
            lastName = itemView.findViewById(R.id.rapid_registration_view_last_name);
            age = itemView.findViewById(R.id.rapid_registration_view_age);
            mobile = itemView.findViewById(R.id.rapid_registration_view_mobile_no);
            email = itemView.findViewById(R.id.rapid_registration_view_email);
            address = itemView.findViewById(R.id.rapid_registration_view_address);
            education = itemView.findViewById(R.id.rapid_registration_view_education);
            disease = itemView.findViewById(R.id.rapid_registration_view_disease);
            motherTongue = itemView.findViewById(R.id.rapid_registration_view_mother_tongue);
            referredByName = itemView.findViewById(R.id.rapid_registration_view_referred_by_name);
            referredByMobile = itemView.findViewById(R.id.rapid_registration_view_referred_by_mobile);
            gender = itemView.findViewById(R.id.rapid_registration_view_gender);

            cardView = itemView.findViewById(R.id.rapid_registration_view_card_view);
        }
    }

}

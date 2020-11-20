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


public class RequestForCounsellingAdapter extends RecyclerView.Adapter<RequestForCounsellingAdapter.RequestForCounsellingViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;
    private View dialog_view;

    public RequestForCounsellingAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestForCounsellingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_request_for_counselling_view_dialog,parent,false);
        dialog_view = LayoutInflater.from(context).inflate(R.layout.check_uncheck_volunteer,parent,false);
        return new RequestForCounsellingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestForCounsellingViewHolder holder, final int position) {

        DatabaseReference request_for_counselling_ref = FirebaseDatabase.getInstance().getReference("request_for_counselling").child(arrayList.get(position));
        request_for_counselling_ref.addValueEventListener(new ValueEventListener() {
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

                    if (snapshot.child("mode").getValue() != null)
                        holder.mode.setText(Objects.requireNonNull(snapshot.child("mode").getValue()).toString());

                    if (snapshot.child("disease").getValue() != null)
                        holder.disease.setText(Objects.requireNonNull(snapshot.child("disease").getValue()).toString());

                    if (snapshot.child("email").getValue() != null)
                        holder.email.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());

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
                                    db_ref.child("checked_request_for_counselling").child(arrayList.get(position)).setValue(true);
                                    db_ref.child("unchecked_request_for_counselling").child(arrayList.get(position)).removeValue();
                                    Toast.makeText(context, "Checked Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                            uncheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference();
                                    db_ref.child("unchecked_request_for_counselling").child(arrayList.get(position)).setValue(true);
                                    db_ref.child("checked_request_for_counselling").child(arrayList.get(position)).removeValue();
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

    public static class RequestForCounsellingViewHolder extends RecyclerView.ViewHolder{
        TextView firstName, lastName, age, mobile, email, disease, gender, mode;
        CardView cardView;

        public RequestForCounsellingViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.request_for_counselling_view_first_name);
            lastName = itemView.findViewById(R.id.request_for_counselling_view_last_name);
            age = itemView.findViewById(R.id.request_for_counselling_view_age);
            mobile = itemView.findViewById(R.id.request_for_counselling_view_mobile_no);
            email = itemView.findViewById(R.id.request_for_counselling_view_email);
            disease = itemView.findViewById(R.id.request_for_counselling_view_disease);
            gender = itemView.findViewById(R.id.request_for_counselling_view_gender);
            mode = itemView.findViewById(R.id.request_for_counselling_view_counselling_mode);

            cardView = itemView.findViewById(R.id.request_for_counselling_view_card_view);
        }
    }
}

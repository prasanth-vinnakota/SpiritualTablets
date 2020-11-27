package developer.prasanth.spiritualtablets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public  class LatestEventsDialog extends AppCompatDialogFragment   {

    private EditText eventName;
    private EditText eventLink;
    private LatestEventListener latestEventListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.latest_events_dialog, null);

        builder.setView(view)
                .setTitle("Enter event details")
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String eventNameString = eventName.getText().toString();
                        String eventLinkString = eventLink.getText().toString();
                        latestEventListener.applyEventsTexts(eventNameString,eventLinkString);
                    }
                });

        eventName = view.findViewById(R.id.latest_event_name);
        eventLink = view.findViewById(R.id.latest_event_link);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            latestEventListener = (LatestEventListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement LatestEventListener");
        }
    }

    public interface LatestEventListener{

        void applyEventsTexts(String event_name, String event_link);
    }
}

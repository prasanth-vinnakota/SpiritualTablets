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

public class AddVideoDialog extends AppCompatDialogFragment {

    private EditText video_name, video_link;
    private AddVideoListener addVideoListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_video_dialog, null);

        builder.setView(view)
                .setTitle("Enter video details")
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String videoName = video_name.getText().toString();
                        String videoLink = video_link.getText().toString();
                        addVideoListener.applyVideoTexts(videoName,videoLink);
                    }
                });
        video_name = view.findViewById(R.id.video_name);
        video_link = view.findViewById(R.id.video_link);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            addVideoListener = (AddVideoListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement AddVideoListener");
        }
    }


    public interface AddVideoListener{

        void applyVideoTexts(String video_name, String video_link);
    }
}


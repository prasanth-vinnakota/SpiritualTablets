package com.example.spiritualtablets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.spiritualtablets.R;

import java.util.ArrayList;

public class FontListAdapter extends ArrayAdapter<String> {

    public FontListAdapter(Context context, ArrayList<String> arrayList){
        super(context, 0 , arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = getItem(position);
        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }

        TextView list_text = convertView.findViewById(R.id.custom_list_TV);

        list_text.setText(item);
        return convertView;
    }
}

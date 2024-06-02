package com.example.sklep_pr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class BrakeSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> brakeModels;
    private Integer[] brakeImages;

    public BrakeSpinnerAdapter(Context context, List<String> brakeModels, Integer[] brakeImages) {
        super(context, R.layout.spinner_item, brakeModels);
        this.context = context;
        this.brakeModels = brakeModels;
        this.brakeImages = brakeImages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text_view);
        ImageView imageView = convertView.findViewById(R.id.image_view);

        textView.setText(brakeModels.get(position));
        if (brakeImages[position] != 0) {
            Glide.with(context).load(brakeImages[position]).into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        return convertView;
    }
}

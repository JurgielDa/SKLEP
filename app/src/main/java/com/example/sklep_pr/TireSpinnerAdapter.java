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

public class TireSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> tireModels;
    private Integer[] tireImages;

    public TireSpinnerAdapter(Context context, List<String> tireModels, Integer[] tireImages) {
        super(context, R.layout.spinner_item, tireModels);
        this.context = context;
        this.tireModels = tireModels;
        this.tireImages = tireImages;
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

        textView.setText(tireModels.get(position));
        if (tireImages[position] != 0) {
            Glide.with(context).load(tireImages[position]).into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        return convertView;
    }
}

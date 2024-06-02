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

public class OilSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> oilModels;
    private Integer[] oilImages;

    public OilSpinnerAdapter(Context context, List<String> oilModels, Integer[] oilImages) {
        super(context, R.layout.spinner_item, oilModels);
        this.context = context;
        this.oilModels = oilModels;
        this.oilImages = oilImages;
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

        textView.setText(oilModels.get(position));
        if (oilImages[position] != 0) {
            Glide.with(context).load(oilImages[position]).into(imageView);
        } else {
            imageView.setImageDrawable(null);
        }

        return convertView;
    }
}

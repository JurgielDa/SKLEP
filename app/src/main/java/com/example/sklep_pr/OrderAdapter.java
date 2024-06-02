package com.example.sklep_pr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<String> {

    public OrderAdapter(Context context, List<String> orderDetails) {
        super(context, 0, orderDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String orderDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_details_item, parent, false);
        }

        TextView orderDetailsTextView = convertView.findViewById(R.id.order_details_text_view);
        TextView orderDateTextView = convertView.findViewById(R.id.order_date_text_view);
        TextView orderNameTextView = convertView.findViewById(R.id.order_name_text_view);
        TextView orderSurnameTextView = convertView.findViewById(R.id.order_surname_text_view);
        TextView orderPriceTextView = convertView.findViewById(R.id.order_price_text_view);

        String[] parts = orderDetail.split(" - ");

        if (parts.length > 0) {
            orderDetailsTextView.setText("Produkty: "+parts[0]);
        } else {
            orderDetailsTextView.setText("");
        }
        if (parts.length > 1) {
            orderDateTextView.setText("Data: "+parts[1]);
        } else {
            orderDateTextView.setText("");
        }
        if (parts.length > 2) {
            orderNameTextView.setText("Imię: "+parts[2]);
        } else {
            orderNameTextView.setText("");
        }
        if (parts.length > 3) {
            orderSurnameTextView.setText("Nazwisko: "+parts[3]);
        } else {
            orderSurnameTextView.setText("");
        }
        if (parts.length > 4) {
            orderPriceTextView.setText("Cena: "+parts[4]+" PLN");
        } else {
            orderPriceTextView.setText("");
        }

        return convertView;
    }
}
package com.example.btlandroidnc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btlandroidnc.Model.Order;
import com.example.btlandroidnc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> orderList;
    private HashMap<Integer, String> orderPushIdMap; // Bản đồ lưu trữ vị trí và pushId của Firebase

    public OrderAdapter(@NonNull Context context, @NonNull List<Order> objects, HashMap<Integer, String> orderPushIdMap) {
        super(context, R.layout.item_order, objects);
        this.context = context;
        this.orderList = objects;
        this.orderPushIdMap = orderPushIdMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        Order order = orderList.get(position);

        TextView textViewProductId = convertView.findViewById(R.id.textViewProductId);
        TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);
        //TextView textViewPriceSale = convertView.findViewById(R.id.textViewPriceSale);
        Button buttonEdit = convertView.findViewById(R.id.buttonEdit);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        textViewProductId.setText("Product ID: " + order.getProduct_id());
        textViewQuantity.setText("Quantity: " + order.getQuantity());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        textViewPrice.setText("Price: " + currencyFormat.format(order.getPrice()));
        //textViewPriceSale.setText("Price Sale: " + currencyFormat.format(order.getPrice_sale()));

        buttonEdit.setOnClickListener(v -> {
            // Handle edit action
            Toast.makeText(context, "Edit order: " + order.getProduct_id(), Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> {
            // Handle delete action
            String pushId = orderPushIdMap.get(position);
            if (pushId != null) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("orders");
                databaseReference.child(pushId).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            orderList.remove(position);
                            orderPushIdMap.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Order deleted", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete order", Toast.LENGTH_SHORT).show());
            }
        });

        return convertView;
    }
}

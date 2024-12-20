package com.example.btlandroidnc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroidnc.Model.Order;
import com.example.btlandroidnc.adapters.OrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ListView orderListView;
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private DatabaseReference databaseReference;
    private HashMap<Integer, String> orderPushIdMap = new HashMap<>(); // Bản đồ lưu trữ vị trí và pushId của Firebase
    ImageButton btTrangChu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Ánh xạ orderListView từ layout
        orderListView = findViewById(R.id.orderListView);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        orderAdapter = new OrderAdapter(this, orderList, orderPushIdMap);
        orderListView.setAdapter(orderAdapter);

        loadOrders();

        btTrangChu = findViewById(R.id.button1);

        btTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, TrangChu.class);
                startActivity(intent);
            }
        });
    }


    private void loadOrders() {
        // Lấy tất cả các đơn hàng từ bảng orders
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                orderPushIdMap.clear();
                int position = 0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    orderList.add(order);
                    orderPushIdMap.put(position, postSnapshot.getKey());
                    position++;
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }




}

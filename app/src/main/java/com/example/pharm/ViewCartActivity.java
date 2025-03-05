package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewCartActivity extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView tvTotalPrice;
    private Button btnPlaceOrder;
    private List<CartItem> cartItems;
    // Inside ViewCartActivity.java, modify the code to use CartManager
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        // Initialize CartManager
        cartManager = new CartManager(this);

        cartItemsContainer = findViewById(R.id.cart_items_container);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        // Get cart items from CartManager
        cartItems = cartManager.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayCartItems();
        updateTotalPrice();

        // Place Order button click listener
        // Inside ViewCartActivity.java
        btnPlaceOrder.setOnClickListener(v -> {
            Intent intent = new Intent(ViewCartActivity.this, OrderActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItems)); // Pass cart items
            intent.putExtra("totalPrice", calculateTotalPrice()); // Pass total price
            startActivity(intent);
        });
    }

    //calculate total price
    private double calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }



    private void displayCartItems() {
        cartItemsContainer.removeAllViews();

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.cart_item_layout, null);

            TextView tvMedicineName = itemView.findViewById(R.id.tv_medicine_name);
            TextView tvPrice = itemView.findViewById(R.id.tv_price);
            TextView tvQuantity = itemView.findViewById(R.id.tv_quantity);
            TextView tvPharmacyName = itemView.findViewById(R.id.tv_pharmacy_name); // Add this TextView in your cart_item_layout.xml
            Button btnRemove = itemView.findViewById(R.id.btn_remove);

            tvMedicineName.setText(item.getMedicineName());
            tvPrice.setText("₹" + item.getTotalPrice());
            tvQuantity.setText(String.valueOf(item.getQuantity()));
            tvPharmacyName.setText("Pharmacy: " + item.getPharmacyName()); // Display pharmacy name

            int finalI = i;
            btnRemove.setOnClickListener(v -> {
                cartItems.remove(finalI);
                displayCartItems();
                updateTotalPrice();
            });

            cartItemsContainer.addView(itemView);
        }
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        tvTotalPrice.setText("Total: ₹" + total);
    }
}

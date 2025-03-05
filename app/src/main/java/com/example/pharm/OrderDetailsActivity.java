package com.example.pharm;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailsActivity extends AppCompatActivity {

    private ListView lvOrders;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        lvOrders = findViewById(R.id.lv_orders);
        dbHelper = new DatabaseHelper(this);

        // Fetch and display order details
        displayOrderDetails();
    }

    private void displayOrderDetails() {
        // Get the current user ID (replace with actual logic to get the user ID)
        int userId = getCurrentUserId();

        // Fetch orders for the current user
        Cursor cursor = dbHelper.getOrdersByUserId(userId);

        if (cursor != null && cursor.moveToFirst()) {
            // Define the columns to display in the ListView
            String[] fromColumns = {
                    DatabaseHelper.COLUMN_ORDER_MEDICINE_NAME,
                    DatabaseHelper.COLUMN_ORDER_PHARMACY_NAME,
                    DatabaseHelper.COLUMN_ORDER_QUANTITY,
                    DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE,
                    DatabaseHelper.COLUMN_ORDER_NAME,
                    DatabaseHelper.COLUMN_ORDER_PHONE,
                    DatabaseHelper.COLUMN_ORDER_ADDRESS,
                    DatabaseHelper.COLUMN_ORDER_DATE,
                    DatabaseHelper.COLUMN_ORDER_STATUS
            };

            // Define the View IDs for each column
            int[] toViews = {
                    R.id.tv_medicine_name,
                    R.id.tv_pharmacy_name,
                    R.id.tv_quantity,
                    R.id.tv_total_price,
                    R.id.tv_name,
                    R.id.tv_phone,
                    R.id.tv_address,
                    R.id.tv_date,
                    R.id.tv_status
            };

            // Create a SimpleCursorAdapter to bind the data to the ListView
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.order_item_layout, // Layout for each item in the ListView
                    cursor,
                    fromColumns,
                    toViews,
                    0
            );

            // Set a custom ViewBinder to format the text
            adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    TextView textView = (TextView) view;
                    String columnName = cursor.getColumnName(columnIndex);
                    String value = cursor.getString(columnIndex);

                    // Format the text based on the column name
                    switch (columnName) {
                        case DatabaseHelper.COLUMN_ORDER_MEDICINE_NAME:
                            textView.setText("" + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        case DatabaseHelper.COLUMN_ORDER_PHARMACY_NAME:
                            textView.setText("" + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        case DatabaseHelper.COLUMN_ORDER_QUANTITY:
                            textView.setText("Quantity: " + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        case DatabaseHelper.COLUMN_ORDER_TOTAL_PRICE:
                            textView.setText("₹" + value);
                            textView.setTextColor(Color.parseColor("#006400"));
                            break;
                        case DatabaseHelper.COLUMN_ORDER_NAME:
                            textView.setText("Buyer Name: " + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        case DatabaseHelper.COLUMN_ORDER_PHONE:
                            textView.setText("Phone: " + value);
                            textView.setTextColor(Color.parseColor("#006400"));
                            break;
                        case DatabaseHelper.COLUMN_ORDER_ADDRESS:
                            textView.setText("Address: " + value);
                            textView.setTextColor(Color.parseColor("#00008B"));
                            break;
                        case DatabaseHelper.COLUMN_ORDER_DATE:
                            textView.setText("" + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        case DatabaseHelper.COLUMN_ORDER_STATUS:
                            textView.setText("Status: " + value);
                            textView.setTextColor(Color.BLACK);
                            break;
                        default:
                            return false; // Let the adapter handle other columns
                    }
                    return true; // We handled the binding
                }
            });

            // Set the adapter to the ListView
            lvOrders.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
        }
    }

    // Replace this with actual logic to get the current user ID
    private int getCurrentUserId() {
        // Example: Retrieve user ID from SharedPreferences or login session
        return 1; // Replace with actual user ID
    }
}
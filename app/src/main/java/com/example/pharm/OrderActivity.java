package com.example.pharm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etAddress;
    private TextView tvPharmacyName, tvMedicines, tvTotalPrice;
    private RadioGroup rgPayment;
    private Button btnBuyNow, btnDownloadInvoice, btnBack;
    private List<CartItem> cartItems;
    private double totalPrice;
    private File invoiceFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize views
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        tvPharmacyName = findViewById(R.id.tv_pharmacy_name); // Add this TextView in your layout
        tvMedicines = findViewById(R.id.tv_medicines);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        rgPayment = findViewById(R.id.rg_payment);
        btnBuyNow = findViewById(R.id.btn_buy_now);
        btnDownloadInvoice = findViewById(R.id.btn_download_invoice);
        btnBack = findViewById(R.id.btn_back);

        // Get data from intent
        Intent intent = getIntent();
        cartItems = (List<CartItem>) intent.getSerializableExtra("cartItems");
        totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

        // Display data
        tvMedicines.setText(getMedicinesList());
        tvTotalPrice.setText("Total Price: ₹" + totalPrice);

        // Display pharmacy name (assuming all items are from the same pharmacy)
        if (!cartItems.isEmpty()) {
            tvPharmacyName.setText("Pharmacy: " + cartItems.get(0).getPharmacyName());
        }

        // Buy Now button click listener
        btnBuyNow.setOnClickListener(v -> placeOrder());

        // Download Invoice button click listener
        btnDownloadInvoice.setOnClickListener(v -> downloadInvoice());

        btnBack.setOnClickListener(v -> {
            Snackbar.make(v, "Logging out...", Snackbar.LENGTH_SHORT).show();
            Intent intent1 = new Intent(OrderActivity.this, Dashboard.class);
            startActivity(intent1);
        });
    }

    private String getMedicinesList() {
        StringBuilder medicinesList = new StringBuilder();
        for (CartItem item : cartItems) {
            medicinesList.append(item.getMedicineName())
                    .append(" (Qty: ")
                    .append(item.getQuantity())
                    .append(")\n");
        }
        return medicinesList.toString();
    }

    private void placeOrder() {
        // Get user input
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected payment method
        int selectedPaymentId = rgPayment.getCheckedRadioButtonId();
        RadioButton rbPayment = findViewById(selectedPaymentId);
        String paymentMethod = rbPayment.getText().toString();
        String paymentStatus = paymentMethod.equals("Pay Online") ? "Order Received (Paid online)" : "Order Received (Not paid, Cash on delivery)";

        // Generate invoice
        invoiceFile = generateInvoice(name, phone, email, address, paymentMethod, paymentStatus);

        // Save order details to the database
        saveOrderToDatabase(name, phone, email, address, paymentMethod, paymentStatus);

        // Enable Download Invoice button
        btnDownloadInvoice.setEnabled(true);

        // Clear the cart after placing the order
        CartManager cartManager = new CartManager(this); // Use 'this' to pass the current context
        cartManager.clearCart();

        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void saveOrderToDatabase(String name, String phone, String email, String address, String paymentMethod, String paymentStatus) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        int userId = getCurrentUserId();
        int pharmacyId = getCurrentPharmacyId();

        for (CartItem item : cartItems) {
            int medicineId = item.getMedicineId();
            String medicineName = item.getMedicineName();
            String pharmacyName = item.getPharmacyName(); // Get pharmacy name from CartItem
            int quantity = item.getQuantity();
            double totalPrice = item.getPricePerUnit() * quantity;

            // Save each item in the cart as a separate order
            boolean isOrderSaved = dbHelper.addOrder(userId, pharmacyId, medicineId, pharmacyName, medicineName, phone, email, quantity, totalPrice, name, paymentStatus, address);

            if (!isOrderSaved) {
                Toast.makeText(this, "Failed to save order for " + item.getMedicineName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Replace these methods with actual implementations to get the current user ID and pharmacy ID
    private int getCurrentUserId() {
        // Implement logic to get the current user ID
        return 1; // Example user ID
    }

    private int getCurrentPharmacyId() {
        // Implement logic to get the current pharmacy ID
        return 1; // Example pharmacy ID
    }

    private File generateInvoice(String name, String phone, String email, String address, String paymentMethod, String paymentStatus) {
        File file = new File(getExternalFilesDir(null), "Invoice.pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add invoice title
            document.add(new Paragraph("Order Invoice\n\n"));

            // Add order details
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell("Name");
            table.addCell(name);

            table.addCell("Phone");
            table.addCell(phone);

            table.addCell("Email");
            table.addCell(email);

            table.addCell("Address");
            table.addCell(address);

            table.addCell("Payment Method");
            table.addCell(paymentMethod);

            table.addCell("Payment Status");
            table.addCell(paymentStatus);

            // Add pharmacy name
            if (!cartItems.isEmpty()) {
                table.addCell("Pharmacy Name");
                table.addCell(cartItems.get(0).getPharmacyName());
            }

            // Add medicines list
            document.add(new Paragraph("\nMedicines:\n"));
            for (CartItem item : cartItems) {
                table.addCell(item.getMedicineName());
                table.addCell("Qty: " + item.getQuantity());
            }

            document.add(table);
            document.add(new Paragraph("\nTotal Price: ₹" + totalPrice));

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private void downloadInvoice() {
        if (invoiceFile != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    FileProvider.getUriForFile(this, getPackageName() + ".provider", invoiceFile),
                    "application/pdf"
            );
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invoice not generated yet", Toast.LENGTH_SHORT).show();
        }
    }
}
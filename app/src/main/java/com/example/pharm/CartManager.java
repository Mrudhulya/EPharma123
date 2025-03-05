package com.example.pharm;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final String CART_PREFS = "cart_prefs";
    private static final String CART_ITEMS_KEY = "cart_items";

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Context context;

    public CartManager(Context context) {
        this.context = context; // Ensure context is properly initialized
        sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addToCart(CartItem item) {
        List<CartItem> cartItems = getCartItems();
        boolean itemExists = false;

        for (CartItem cartItem : cartItems) {
            if (cartItem.getMedicineName().equals(item.getMedicineName())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cartItems.add(item);
        }

        saveCartItems(cartItems);
    }

    public List<CartItem> getCartItems() {
        String json = sharedPreferences.getString(CART_ITEMS_KEY, null);
        Type type = new TypeToken<ArrayList<CartItem>>() {}.getType();
        return gson.fromJson(json, type) != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    private void saveCartItems(List<CartItem> cartItems) {
        String json = gson.toJson(cartItems);
        sharedPreferences.edit().putString(CART_ITEMS_KEY, json).apply();
    }

    public void clearCart() {
        if (context != null) { // Ensure context is not null
            SharedPreferences sharedPreferences = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CART_ITEMS_KEY); // Clear all cart items
            editor.apply();
        } else {
            throw new IllegalStateException("Context is null in CartManager.clearCart()");
        }
    }
}
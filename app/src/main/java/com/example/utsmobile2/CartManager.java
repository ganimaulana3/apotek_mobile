package com.example.utsmobile2;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class CartManager {
    private static final String PREF_NAME = "cart_pref";
    private static final String KEY_CART = "cart_list";

    // ✅ Allow adding without login
    public static void addToCart(Context context, Produk produkBaru) {
        SharedPreferences loginPrefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String id_user = loginPrefs.getString("id_user", "guest"); // Default to guest

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        List<Produk> cart = getCart(context);

        boolean produkDitemukan = false;

        for (Produk p : cart) {
            if (p.getId_produk().equals(produkBaru.getId_produk()) && id_user.equals(p.getId_user())) {
                p.setQuantity(p.getQuantity() + 1);
                produkDitemukan = true;
                break;
            }
        }

        if (!produkDitemukan) {
            produkBaru.setQuantity(1);
            produkBaru.setId_user(id_user); // Pasang id_user, bisa "guest"
            cart.add(produkBaru);
        }

        String json = gson.toJson(cart);
        prefs.edit().putString(KEY_CART, json).apply();
    }

    public static List<Produk> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CART, null);

        if (json != null) {
            Type type = new TypeToken<List<Produk>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static List<Produk> getCartByUser(Context context) {
        SharedPreferences loginPrefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String id_user = loginPrefs.getString("id_user", null);

        List<Produk> allCart = getCart(context);
        List<Produk> userCart = new ArrayList<>();

        if (id_user != null) {
            for (Produk p : allCart) {
                if (id_user.equals(p.getId_user())) {
                    userCart.add(p);
                }
            }
        }

        return userCart;
    }

    public static void clearCart(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().remove(KEY_CART).apply();
    }

    public static void removeFromCart(Context context, Produk produk) {
        List<Produk> cart = getCart(context);
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId_produk().equals(produk.getId_produk())) {
                cart.remove(i);
                break;
            }
        }
        saveCart(context, cart);
    }

    private static void saveCart(Context context, List<Produk> cart) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        prefs.edit().putString(KEY_CART, json).apply();
    }

    // ✅ New method: Attach guest cart to logged-in user
    public static void updateGuestCartToUser(Context context, String newUserId) {
        List<Produk> cart = getCart(context);
        boolean changed = false;

        for (Produk p : cart) {
            if (p.getId_user().equals("guest")) {
                p.setId_user(newUserId);
                changed = true;
            }
        }

        if (changed) {
            saveCart(context, cart);
        }
    }
}

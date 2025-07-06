package com.example.utsmobile2.ui.keranjang;

public class CartItem {
    private String name; // Nama produk
    private double price; // Harga produk
    private int quantity; // Jumlah produk
    private int imageResId; // Resource ID untuk gambar produk

    // Constructor
    public CartItem(String name, double price, int quantity, int imageResId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    // Getter untuk nama produk
    public String getName() {
        return name;
    }

    // Getter untuk harga produk
    public double getPrice() {
        return price;
    }

    // Getter untuk jumlah produk
    public int getQuantity() {
        return quantity;
    }

    // Setter untuk jumlah produk
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter untuk resource ID gambar produk
    public int getImageResId() {
        return imageResId;
    }
}

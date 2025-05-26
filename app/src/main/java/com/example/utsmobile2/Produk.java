package com.example.utsmobile2;

public class Produk {
    private String id_produk;
    private String nm_produk;
    private String kategori;
    private String deskripsi;
    private String harga;
    private int stok;
    private String img_produk;
    private int total_views;

    public void setTotal_views(int total_views) {
        this.total_views = total_views;
    }

    private int quantity = 1;
    private String id_user; // Tambahkan ini

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter (dan setter jika perlu)
    public String getId_produk() { return id_produk; }
    public String getNm_produk() { return nm_produk; }
    public String getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getImg_produk() { return img_produk; }
    public String getDeskripsi() { return deskripsi; }
    public String getKategori() { return kategori; }
    public int getTotal_views() {
        return total_views;
    }
}

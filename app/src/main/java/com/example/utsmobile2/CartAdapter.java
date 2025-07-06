package com.example.utsmobile2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Produk> cartList;
    private Context context;

    public interface OnItemDeleteListener {
        void onDelete(Produk produk);
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }
    private OnQuantityChangedListener quantityChangedListener;

    private OnItemDeleteListener deleteListener;

    public CartAdapter(Context context, List<Produk> cartList, OnItemDeleteListener deleteListener, OnQuantityChangedListener quantityChangedListener) {
        this.context = context;
        this.cartList = cartList;
        this.deleteListener = deleteListener;
        this.quantityChangedListener = quantityChangedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQty, txtHarga;
        ImageButton btnDelete;
        ImageView imgProduct;
        Button btnDecrease, btnIncrease; // Add buttons for quantity control

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtQty = itemView.findViewById(R.id.txtCartQty);
            txtHarga = itemView.findViewById(R.id.tvHarga);
            btnDelete = itemView.findViewById(R.id.btnCartDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease); // Initialize decrease button
            btnIncrease = itemView.findViewById(R.id.btnIncrease); // Initialize increase button
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Produk produk = cartList.get(position);
        holder.txtName.setText(produk.getNm_produk());
        holder.txtQty.setText("Qty: " + produk.getQuantity());

        // Error handling for price parsing
        int hargaSatuan;
        try {
            hargaSatuan = Integer.parseInt(produk.getHarga());
        } catch (NumberFormatException e) {
            hargaSatuan = 0; // Default to 0 if parsing fails
        }

        int totalHarga = hargaSatuan * produk.getQuantity();
        holder.txtHarga.setText(String.format("Rp %,d", totalHarga)); // Format price with commas

        String url = ApiClient.getBaseUrl();
        String imgUrl = url + "img_produk/" + produk.getImg_produk();

        Glide.with(context)
                .load(imgUrl)
                .into(holder.imgProduct);

        holder.btnDelete.setOnClickListener(v -> {
            deleteListener.onDelete(produk);
        });

        // Set up click listeners for quantity buttons
        int finalHargaSatuan = hargaSatuan;
        holder.btnDecrease.setOnClickListener(v -> {
            if (produk.getQuantity() > 1) {
                int newQuantity = produk.getQuantity() - 1;
                produk.setQuantity(newQuantity);
                CartManager.updateQuantityInCart(context, produk, newQuantity);
                holder.txtQty.setText("Qty: " + newQuantity);
                holder.txtHarga.setText(String.format("Rp %,d", finalHargaSatuan * newQuantity));
                if (quantityChangedListener != null) {
                    quantityChangedListener.onQuantityChanged(); // Notify the fragment
                }
            }
        });
        int finalHargaSatuan1 = hargaSatuan;
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = produk.getQuantity() + 1;
            produk.setQuantity(newQuantity);
            CartManager.updateQuantityInCart(context, produk, newQuantity);
            holder.txtQty.setText("Qty: " + newQuantity);
            holder.txtHarga.setText(String.format("Rp %,d", finalHargaSatuan1 * newQuantity));
            if (quantityChangedListener != null) {
                quantityChangedListener.onQuantityChanged(); // Notify the fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}

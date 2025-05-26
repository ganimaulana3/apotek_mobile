package com.example.utsmobile2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private OnItemDeleteListener deleteListener;

    public CartAdapter(Context context, List<Produk> cartList, OnItemDeleteListener deleteListener) {
        this.context = context;
        this.cartList = cartList;
        this.deleteListener = deleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQty, txtHarga, total;
        Button btnDelete;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtQty = itemView.findViewById(R.id.txtCartQty);
            txtHarga = itemView.findViewById(R.id.tvHarga);
            total = itemView.findViewById(R.id.totalCart);
            btnDelete = itemView.findViewById(R.id.btnCartDelete);
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
        int hargaSatuan = Integer.parseInt(produk.getHarga());
        int totalHarga = hargaSatuan * produk.getQuantity();
        holder.txtHarga.setText("Rp " + totalHarga);

        String url = ApiClient.getBaseUrl();
        String imgUrl = url + "img_produk/" + produk.getImg_produk();

        Glide.with(context)
                .load(imgUrl)
                .into(holder.imgProduct);

        holder.btnDelete.setOnClickListener(v -> {
            deleteListener.onDelete(produk);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}

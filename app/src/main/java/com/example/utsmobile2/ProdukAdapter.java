package com.example.utsmobile2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Produk> produkList;
    private List<Produk> produkListFull;
    private TextView txtNoResult;
    private String id_user;

    public ProdukAdapter(Context context, List<Produk> produkList, TextView txtNoResult) {
        this.context = context;
        this.produkList = produkList;
        this.produkListFull = new ArrayList<>(produkList);
        this.txtNoResult = txtNoResult;
        this.id_user = id_user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produk produk = produkList.get(position);

        holder.txtName.setText(produk.getNm_produk());
        holder.txtPrice.setText("Rp " + produk.getHarga());
        holder.txtStatus.setText(produk.getStok() > 0 ? "Tersedia" : "Tidak Tersedia");
        holder.txtStatus.setTextColor(
                produk.getStok() > 0 ? Color.GREEN : Color.RED
        );
        holder.txtVisitor.setText(" " + (produk.getTotal_views() > 0 ? produk.getTotal_views() : 0) + " ");

        // Load gambar dari folder lokal server
        String url = ApiClient.getBaseUrl();
        String imgUrl = url + "img_produk/" + produk.getImg_produk();
        Glide.with(context).load(imgUrl).into(holder.imgProduct);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProduk.class);
                intent.putExtra("id_produk", produk.getId_produk()); // Kirim ID produk
                context.startActivity(intent);
            }
        });
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produk.getStok() > 0) {
                    // Tambahkan ke keranjang
                    CartManager.addToCart(context, produk);

                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Ditambahkan ke Keranjang")
                            .setContentText("Produk \"" + produk.getNm_produk() + "\" telah masuk ke keranjang.")
                            .setConfirmText("OK")
                            .show();
                } else {
                    // Produk tidak tersedia
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Produk Habis")
                            .setContentText("Maaf, produk \"" + produk.getNm_produk() + "\" sedang tidak tersedia.")
                            .setConfirmText("OK")
                            .show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnDetail, btnCart;
        TextView txtName, txtPrice, txtStatus, txtVisitor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnCart = itemView.findViewById(R.id.btnAddToCart);
            txtVisitor = itemView.findViewById(R.id.viewed);
        }
    }

    private final Filter produkFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Produk> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(produkListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Produk item : produkListFull) {
                    if (item.getNm_produk().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            produkList.clear();
            produkList.addAll((List<Produk>) results.values);
            notifyDataSetChanged();

            if (txtNoResult != null) {
                txtNoResult.setVisibility(produkList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        }
    };

    @Override
    public Filter getFilter() {
        return produkFilter;
    }



}

package com.example.utsmobile2.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsmobile2.BuktiUploadCallback;
import com.example.utsmobile2.R;
import com.example.utsmobile2.model.HistoryOrder;

import java.util.List;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.HistoryViewHolder> {

    private Context context;
    private List<HistoryOrder> orderList;
    private BuktiUploadCallback callback;

    public interface OnUploadClickListener {
        void onItemClick(HistoryOrder order, ImageView imageView);
    }

    public HistoryOrderAdapter(Context context, List<HistoryOrder> orderList, BuktiUploadCallback callback) { // Update constructor
        this.context = context;
        this.orderList = orderList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_order, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryOrder order = orderList.get(position);

        holder.tvId.setText("ID Order: #" + order.trans_id);
        holder.tvNmPenerima.setText("Nama: " + order.nm_penerima);
        holder.tvTanggal.setText("Tanggal: " + order.tgl_order);
        holder.tvTotal.setText("Total: Rp" + order.total_bayar);
        holder.tvAlamat.setText("Alamat: " + order.alamat_kirim + ", " + order.kota + ", Provinsi " + order.provinsi);
        holder.tvKodePos.setText("Kode POS: " + order.kodepos);
        holder.tvOngkir.setText("Ongkir: Rp" + order.ongkir);
        holder.tvSubtotal.setText("Subtotal: Rp" + order.subtotal);
        holder.tvMetodeBayar.setText("Metode Bayar: " + (order.metodebayar == 1 ? "COD" : "Bank Transfer"));
        holder.estimasi.setText("Estimasi Pengiriman: " + order.lamakirim);

        // Set expand/collapse state
        holder.layoutDetail.setVisibility(order.isExpanded() ? View.VISIBLE : View.GONE);

        holder.layoutRingkasan.setOnClickListener(v -> {
            boolean expanded = !order.isExpanded();
            order.setExpanded(expanded);
            notifyItemChanged(position);
        });

        if (order.selectedImageUri != null) {
            holder.previewImage.setImageURI(order.selectedImageUri);
            holder.previewImage.setVisibility(View.VISIBLE);
        } else {
            holder.previewImage.setVisibility(View.GONE);
        }


        // Status Label
        String statusText;
        switch (order.status) {
            case 0:
                statusText = "Menunggu Pembayaran";
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.delete_red));
                break;
            case 1:
                statusText = "Status: Proses";
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.orange));
                break;
            case 2:
                statusText = "Status: Dikirim";
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.blue));
                break;
            case 3:
                statusText = "Status: Diterima";
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                break;
            default:
                statusText = "Tidak diketahui";
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                break;
        }
        if (order.metodebayar == 1){
            holder.btnUpload.setVisibility(View.GONE);
            holder.uploadInfo.setVisibility(View.GONE);
            holder.tvBankInfo.setVisibility(View.GONE);
        } else {
            holder.btnUpload.setVisibility(View.VISIBLE);
            holder.uploadInfo.setVisibility(View.VISIBLE);
            holder.tvBankInfo.setVisibility(View.VISIBLE);
        }

            holder.tvStatus.setText(statusText);
        if (order.status == 1) {
            holder.lunas.setVisibility(View.GONE);
        } else {
            holder.lunas.setVisibility(View.VISIBLE); // Hide the 'lunas' view if status is 1 or 2
        }
        if (order.status == 0){
            holder.lunas.setVisibility(View.GONE);
            holder.btnUpload.setOnClickListener(v -> {
                callback.onUploadClick(order.trans_id); // Save trans_id first
                // Only trigger upload if an image is selected
                if (order.selectedImageUri != null) {
                    callback.onUploadTrigger(); // Trigger upload
                } else {
                    Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnUpload.setVisibility(View.GONE);
            holder.uploadInfo.setVisibility(View.GONE);
            holder.tvBankInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public void updateImage(int transId, Uri imageUri) {
        for (int i = 0; i < orderList.size(); i++) {
            HistoryOrder order = orderList.get(i);
            if (order.trans_id == transId) {
                order.selectedImageUri = imageUri; // TAMBAH FIELD DI MODEL
                notifyItemChanged(i); // Ini akan trigger onBindViewHolder
                break;
            }
        }
    }


    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTanggal, estimasi, tvMetodeBayar, tvOngkir, tvTotal, tvAlamat, tvBuktiBayar, tvStatus, tvKodePos, tvSubtotal,
                tvBankInfo, uploadInfo, tvNmPenerima;
//        Button btnUpload;
        ImageView previewImage;
        LinearLayout layoutRingkasan, layoutDetail, btnUpload, lunas;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId);
            tvTanggal = itemView.findViewById(R.id.tvTanggalOrder);
            tvTotal = itemView.findViewById(R.id.tvTotalBayar);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
            tvKodePos = itemView.findViewById(R.id.tvKodepos);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            tvOngkir = itemView.findViewById(R.id.tvOngkir);
            estimasi = itemView.findViewById(R.id.tvEstimasi);
            tvMetodeBayar = itemView.findViewById(R.id.tvMetodeBayar);
            btnUpload = itemView.findViewById(R.id.uploadContainer);
            tvBuktiBayar = itemView.findViewById(R.id.tvBuktiBayar);
            layoutRingkasan = itemView.findViewById(R.id.layoutRingkasan);
            layoutDetail = itemView.findViewById(R.id.layoutDetail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            previewImage = itemView.findViewById(R.id.previewImage);
            lunas = itemView.findViewById(R.id.statusLunas);
            tvBankInfo = itemView.findViewById(R.id.tvBankInfo);
            uploadInfo = itemView.findViewById(R.id.uploadInfo);
            tvNmPenerima = itemView.findViewById(R.id.tvNmPenerima);
        }
    }
}

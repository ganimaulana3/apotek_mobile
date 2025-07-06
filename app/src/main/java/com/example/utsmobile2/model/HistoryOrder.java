package com.example.utsmobile2.model;

import android.net.Uri;

import java.util.List;

public class HistoryOrder {
    public int trans_id;
    public String tgl_order, nm_penerima, alamat_kirim, kota, provinsi, kodepos, lamakirim, buktibayar;
    public int subtotal, ongkir, total_bayar, metodebayar, status;
    public List<OrderDetail> detail;
    public Uri selectedImageUri;
    public boolean isExpanded = false;

    public int getTrans_id() {
        return trans_id;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }
}


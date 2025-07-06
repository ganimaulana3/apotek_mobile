package com.example.utsmobile2;

import android.widget.ImageView;

import com.example.utsmobile2.model.HistoryOrder;

public interface BuktiUploadCallback {
    void onUploadClick(int transId);  // Untuk tahu order mana
    void onUploadTrigger();           // Untuk trigger uploadBuktiBayar()
}



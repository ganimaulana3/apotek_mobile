package com.example.utsmobile2.ui.home.kategori;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.ProdukAdapter;
import com.example.utsmobile2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kategori3 extends AppCompatActivity {
    RecyclerView rvKategori;
    List<Produk> produkList = new ArrayList<>();
    TextView txtNoResult;
    ProdukAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kategori3);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        txtNoResult = findViewById(R.id.kategori3);
        rvKategori = findViewById(R.id.rvKategori3);
        rvKategori.setLayoutManager(new GridLayoutManager(Kategori3.this, 2));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getKategori3().enqueue(new Callback<List<Produk>>() {
            @Override
            public void onResponse(Call<List<Produk>> call, Response<List<Produk>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produkList = response.body();
                    adapter = new ProdukAdapter(Kategori3.this, produkList, txtNoResult); // ← PERBAIKAN DI SINI
                    rvKategori.setAdapter(adapter);
                } else {
                    Toast.makeText(Kategori3.this, "Data kosong atau tidak sesuai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Produk>> call, Throwable t) {
                Toast.makeText(Kategori3.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show(); // ← PERBAIKAN DI SINI
            }
        });
    }
}
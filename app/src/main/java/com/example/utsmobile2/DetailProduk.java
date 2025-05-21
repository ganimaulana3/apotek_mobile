package com.example.utsmobile2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProduk extends AppCompatActivity {
    ImageView imgProduk, btnBack;
    Button btnCart;
    private Produk produk;
    private Context context;
    TextView txtNama, txtHarga, txtStatus, txtDeskripsi, txtKategori;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_produk);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context = this;

        imgProduk = findViewById(R.id.imgProduk);
        txtNama = findViewById(R.id.txtNama);
        txtHarga = findViewById(R.id.txtHarga);
        txtStatus = findViewById(R.id.txtStatus);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtKategori = findViewById(R.id.txtKategori);
        btnCart = (Button) findViewById(R.id.btnAddToCart);

        String idProduk = getIntent().getStringExtra("id_produk");
        if (idProduk != null) {
            loadDetailProduk(idProduk);
        }

        btnBack = (ImageView) findViewById(R.id.detailBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProduk.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadDetailProduk(String idProduk) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Produk> call = apiService.getDetailProduk(idProduk);

        call.enqueue(new Callback<Produk>() {
            @Override
            public void onResponse(Call<Produk> call, Response<Produk> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Produk produk = response.body();
                    DetailProduk.this.produk = response.body();
                    SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                    String id_user = prefs.getString("id_user", null);

                    txtNama.setText(produk.getNm_produk());
                    txtHarga.setText("Rp " + produk.getHarga());
                    txtDeskripsi.setText(produk.getDeskripsi());
                    txtKategori.setText(produk.getKategori());

                    // Status berdasarkan stok
                    int stok = produk.getStok();
                    if (stok > 0) {
                        txtStatus.setText("Tersedia");
                        txtStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else {
                        txtStatus.setText("Tidak Tersedia");
                        txtStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }

                    // Load gambar pakai Glide
                    String imageUrl = "http://172.20.10.8/android/img_produk/" + produk.getImg_produk();

                    Glide.with(DetailProduk.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(imgProduk);

                    if (id_user != null) {
                        ApiService apiService = ApiClient.getClient().create(ApiService.class);
                        Call<ResponseBody> callViewer = apiService.addViewer(id_user, produk.getNm_produk());

                        callViewer.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                // Viewer berhasil ditambahkan / diupdate
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                // Log kalau gagal
                            }
                        });
                    }

                }
            }



            @Override
            public void onFailure(Call<Produk> call, Throwable t) {
                Toast.makeText(DetailProduk.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
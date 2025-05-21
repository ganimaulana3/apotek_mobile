package com.example.utsmobile2;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);

        // Ambil ID user dari shared preferences
        String idUser = sharedPreferences.getString("id_user", "");

        TextInputEditText etNama = findViewById(R.id.etNama);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etAlamat = findViewById(R.id.etAlamat);
        TextInputEditText etKota = findViewById(R.id.etKota);
        TextInputEditText etProvinsi = findViewById(R.id.etProvinsi);
        TextInputEditText etTelp = findViewById(R.id.etTelp);
        TextInputEditText etKodepos = findViewById(R.id.etKodepos);
        MaterialButton btnSave = findViewById(R.id.btnSave);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.8/android/") // ganti IP sesuai dengan lokal/server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);

        // Ambil data user berdasarkan id
        api.getUser(idUser).enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().getUser();
                    etNama.setText(user.getNama());
                    etEmail.setText(user.getEmail());
                    etAlamat.setText(user.getAlamat());
                    etKota.setText(user.getKota());
                    etProvinsi.setText(user.getProvinsi());
                    etTelp.setText(user.getTelp());
                    etKodepos.setText(user.getKodepos());
                } else {
                    Toast.makeText(EditProfile.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(EditProfile.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String nama = etNama.getText().toString().trim();
            String alamat = etAlamat.getText().toString().trim();
            String kota = etKota.getText().toString().trim();
            String provinsi = etProvinsi.getText().toString().trim();
            String telp = etTelp.getText().toString().trim();
            String kodepos = etKodepos.getText().toString().trim();

            if (email.isEmpty() || nama.isEmpty() || alamat.isEmpty() || kota.isEmpty() ||
                    provinsi.isEmpty() || telp.isEmpty() || kodepos.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            api.updateProfile(email, nama, alamat, kota, provinsi, telp, kodepos)
                    .enqueue(new Callback<ResponseUpdate>() {
                        @Override
                        public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                            if (response.isSuccessful()) {
                                ResponseUpdate res = response.body();
                                if (res != null && res.getResult() == 1) {
                                    Toast.makeText(EditProfile.this, "Profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditProfile.this, res != null ? res.getMessage() : "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                            Toast.makeText(EditProfile.this, "Kesalahan jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}
package com.example.utsmobile2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
    private TextInputEditText etNama, etEmail, etAlamat, etKota, etProvinsi, etTelp, etKodepos;
    private ImageView imgProfile;
    private Uri selectedImageUri;
    private String fileName = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences sharedPreferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();

        sharedPreferences = getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("id_user", "");

        getUserData(userId);

        findViewById(R.id.btnEditFoto).setOnClickListener(v -> chooseImage());
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadProfileWithImage();
            } else {
                uploadProfileWithoutImage();
            }

        });

    }

    private void initViews() {
        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etAlamat = findViewById(R.id.etAlamat);
        etKota = findViewById(R.id.etKota);
        etProvinsi = findViewById(R.id.etProvinsi);
        etTelp = findViewById(R.id.etTelp);
        etKodepos = findViewById(R.id.etKodepos);
        imgProfile = findViewById(R.id.imgProfile);
    }

    private boolean isValid() {
        return !etNama.getText().toString().trim().isEmpty() &&
                !etEmail.getText().toString().trim().isEmpty() &&
                !etAlamat.getText().toString().trim().isEmpty() &&
                !etKota.getText().toString().trim().isEmpty() &&
                !etProvinsi.getText().toString().trim().isEmpty() &&
                !etTelp.getText().toString().trim().isEmpty() &&
                !etKodepos.getText().toString().trim().isEmpty();
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            fileName = getFileName(selectedImageUri);
            imgProfile.setImageURI(selectedImageUri);
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }

    private void getUserData(String idUser) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);
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
                    String baseUrlFoto = ApiClient.getBaseUrl() + "profile/"; // Sesuaikan dengan path folder upload di server
                    String foto = user.getFoto();

                    if (foto != null && !foto.isEmpty()) {
                        int radius = 30; // dalam pixel, bisa kamu sesuaikan

                        Glide.with(EditProfile.this)
                                .load(ApiClient.getBaseUrl() + "profile/" + foto)
                                .transform(new RoundedCorners(radius))
                                .placeholder(R.drawable.baseline_person_24)
                                .into(imgProfile);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(EditProfile.this, "Gagal ambil data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadProfileWithoutImage() {
        String url = ApiClient.getBaseUrl() + "post_profile.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Gagal update: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", userId);
                params.put("email", etEmail.getText().toString().trim());
                params.put("nama", etNama.getText().toString().trim());
                params.put("alamat", etAlamat.getText().toString().trim());
                params.put("kota", etKota.getText().toString().trim());
                params.put("provinsi", etProvinsi.getText().toString().trim());
                params.put("telp", etTelp.getText().toString().trim());
                params.put("kodepos", etKodepos.getText().toString().trim());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    private void uploadProfileWithImage() {
        try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] imageData = getBytes(iStream);
                String url = ApiClient.getBaseUrl() + "post_profile.php";

                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        response -> {
                            Toast.makeText(this, "Profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                            finish();
                        },
                        error -> {
                            Toast.makeText(this, "Gagal update: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                Map<String, String> stringParams = new HashMap<>();
                stringParams.put("id_user", userId);
                stringParams.put("email", etEmail.getText().toString().trim());
                stringParams.put("nama", etNama.getText().toString().trim());
                stringParams.put("alamat", etAlamat.getText().toString().trim());
                stringParams.put("kota", etKota.getText().toString().trim());
                stringParams.put("provinsi", etProvinsi.getText().toString().trim());
                stringParams.put("telp", etTelp.getText().toString().trim());
                stringParams.put("kodepos", etKodepos.getText().toString().trim());
                multipartRequest.setParams(stringParams);

                Map<String, VolleyMultipartRequest.DataPart> byteParams = new HashMap<>();
                byteParams.put("foto", new VolleyMultipartRequest.DataPart(fileName, imageData, "image/jpeg"));
                multipartRequest.setByteData(byteParams);

                Volley.newRequestQueue(this).add(multipartRequest);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membaca file gambar", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
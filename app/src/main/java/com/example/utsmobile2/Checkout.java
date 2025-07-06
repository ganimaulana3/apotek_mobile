package com.example.utsmobile2;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Checkout extends AppCompatActivity {
    EditText etEmail, etAlamat, etTelp, etKota, etProvinsi, etLamaKirim, etKodePos,
            etMetodeBayar, etSubtotal, etOngkir, etTotalBayar, etBuktiBayar, etStatus, etNm;
    Button btnCheckout, btnPilihGambar;
    ImageView previewImage;
    Spinner spMetodeBayar, spProvinsi, spKota;
    ArrayList<String> provinsiList = new ArrayList<>();
    ArrayList<String> provinsiIdList = new ArrayList<>();
    ArrayList<String> kotaList = new ArrayList<>();
    ArrayList<String> kotaIdList = new ArrayList<>();
    ArrayAdapter<String> provinsiAdapter, kotaAdapter;
    int metodeInt = 0;
    int subtotal = 0;
    Uri filePath;
    String fileName = "";
    final int PICK_IMAGE_REQUEST = 1;
    String uploadUrl = "http://192.168.1.57/android/post_order.php";
    SharedPreferences sharedPreferences;
    String nmUser, emailUser;
    String baseUrl = ApiClient.getBaseUrl();

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        sharedPreferences = getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);
        nmUser = sharedPreferences.getString("nama", "");
        emailUser = sharedPreferences.getString("email", "");

        initViews();
        loadProvinsi();
        spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvId = provinsiIdList.get(position);
                loadKota(selectedProvId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String origin = "399"; // ID kota asal, tetap
            int berat = 1000; // 1 kg
            String kurir = "jne";

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < kotaIdList.size()) {
                    String destId = kotaIdList.get(position);
                    cekOngkir(origin, destId, berat, kurir);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        spMetodeBayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Hint terpilih, jangan proses
                    metodeInt = 0;
                } else {
                    metodeInt = position; // Mandiri = 1, BRI = 2
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        FrameLayout uploadContainer = findViewById(R.id.uploadContainer);

        uploadContainer.setOnClickListener(v -> chooseImage());
        btnCheckout.setOnClickListener(v -> {
            try {
                uploadCheckoutWithoutImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void initViews() {
        etNm = findViewById(R.id.etNmPenerima);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setText(String.valueOf(emailUser));
        etAlamat = findViewById(R.id.etAlamat);
        etTelp = findViewById(R.id.etTelp);

        spProvinsi = findViewById(R.id.spProvinsi);
        provinsiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinsiList);
        provinsiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvinsi.setAdapter(provinsiAdapter);

        spKota = findViewById(R.id.spKota);
        kotaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kotaList);
        kotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKota.setAdapter(kotaAdapter);

        etLamaKirim = findViewById(R.id.etLamaKirim);
        etKodePos = findViewById(R.id.etKodePos);

        spMetodeBayar = findViewById(R.id.spMetodeBayar);
        String[] metodeList = {"Pilih Metode Bayar", "COD", "Mandiri - 1770018170891"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMetodeBayar.setAdapter(adapter);

        etSubtotal = findViewById(R.id.etSubtotal);
        int subtotal = getIntent().getIntExtra("subtotal", 0);
        etSubtotal.setText(String.valueOf(subtotal));

        etOngkir = findViewById(R.id.etOngkir);
        etTotalBayar = findViewById(R.id.etTotalBayar);
//        etStatus = findViewById(R.id.etStatus);
        btnCheckout = findViewById(R.id.btnCheckout);
//        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        previewImage = findViewById(R.id.previewImage);
    }

    private void cekOngkir(String originId, String destinationId, int weight, String courier) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + "api_ongkir.php",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray costs = jsonObject.getJSONObject("rajaongkir")
                                .getJSONArray("results").getJSONObject(0)
                                .getJSONArray("costs");

                        if (costs.length() > 0) {
                            JSONObject layanan = costs.getJSONObject(0); // Ambil layanan pertama
                            JSONObject costDetail = layanan.getJSONArray("cost").getJSONObject(0);

                            int ongkir = costDetail.getInt("value");
                            String estimasi = costDetail.getString("etd");

                            etOngkir.setText(String.valueOf(ongkir));
                            int subtotal2 = getIntent().getIntExtra("subtotal", 0);
                            int totalBayar = subtotal2 + ongkir;
                            etTotalBayar.setText(String.valueOf(totalBayar));
                            etLamaKirim.setText(estimasi + " hari");
                            Toast.makeText(this, "Ongkir ditemukan: " + ongkir, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing ongkir", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal ambil ongkir", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("origin", originId);         // ID kota asal (misal: "501")
                params.put("destination", destinationId); // ID kota tujuan
                params.put("weight", String.valueOf(weight)); // dalam gram, misal 1000
                params.put("courier", courier); // misal "jne"
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadKota(String provId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl + "get_kota.php?province_id=" + provId,
                response -> {
                    try {
                        kotaList.clear();
                        kotaIdList.clear();

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONObject("rajaongkir").getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject kota = results.getJSONObject(i);
                            kotaList.add(kota.getString("type")+" "+kota.getString("city_name"));
                            kotaIdList.add(kota.getString("city_id"));
                        }

                        kotaAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing kota", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal memuat kota", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void loadProvinsi() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl + "get_provinsi.php",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray results = jsonObject.getJSONObject("rajaongkir").getJSONArray("results");

                        provinsiList.clear();
                        provinsiIdList.clear();

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject prov = results.getJSONObject(i);
                            provinsiList.add(prov.getString("province"));
                            provinsiIdList.add(prov.getString("province_id"));
                        }

                        provinsiAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing provinsi", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal ambil data provinsi", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Pilih Bukti Pembayaran"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            fileName = getFileName(filePath);
            previewImage.setImageURI(filePath);
            previewImage.setVisibility(View.VISIBLE);
            Toast.makeText(this, "File dipilih: " + fileName, Toast.LENGTH_SHORT).show();
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

    private void uploadCheckoutWithImage() throws IOException {
        InputStream iStream = getContentResolver().openInputStream(filePath);
        byte[] imageData = getBytes(iStream);

        JSONObject order = new JSONObject();
        JSONArray orderDetail = new JSONArray();

        try {
            // Bangun JSON order
            order.put("email", etEmail.getText().toString().trim());
            order.put("alamat_kirim", etAlamat.getText().toString().trim());
            order.put("telp_kirim", etTelp.getText().toString().trim());
            order.put("kota", spKota.getSelectedItem().toString().trim());
            order.put("provinsi", spProvinsi.getSelectedItem().toString().trim());
            order.put("lamakirim", etLamaKirim.getText().toString().trim());
            order.put("kodepos", etKodePos.getText().toString().trim());
            order.put("metodebayar", metodeInt);
            order.put("subtotal", Integer.parseInt(etSubtotal.getText().toString()));
            order.put("ongkir", Integer.parseInt(etOngkir.getText().toString()));
            order.put("total_bayar", Integer.parseInt(etTotalBayar.getText().toString()));
//            order.put("status", Integer.parseInt(etStatus.getText().toString()));
            List<Produk> cartList = CartManager.getCartByUser(this);
            // Contoh order_detail statis
            for (Produk produk : cartList) {
                JSONObject item = new JSONObject();
                item.put("kode", produk.getId_produk());
                item.put("harga", Integer.parseInt(produk.getHarga()));
                item.put("qty", produk.getQuantity());
                orderDetail.put(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl,
                response -> {
                    Toast.makeText(this, "Checkout Berhasil", Toast.LENGTH_SHORT).show();

// ✅ Hapus semua cart setelah checkout
                    CartManager.clearCart(this);

// ✅ Redirect ke Home atau MainActivity
                    Intent intent = new Intent(this, MainActivity.class); // ganti ke activity Home milikmu
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // tutup halaman checkout

                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Map<String, String> stringParams = new HashMap<>();
        stringParams.put("order", order.toString());
        stringParams.put("order_detail", orderDetail.toString());
        multipartRequest.setParams(stringParams);

        Map<String, VolleyMultipartRequest.DataPart> byteParams = new HashMap<>();
        byteParams.put("bukti", new VolleyMultipartRequest.DataPart(fileName, imageData, "image/jpeg"));
        multipartRequest.setByteData(byteParams);

        Volley.newRequestQueue(this).add(multipartRequest);

    }

    private void uploadCheckoutWithoutImage() {
        JSONObject order = new JSONObject();
        JSONArray orderDetail = new JSONArray();

        try {
            // Bangun JSON order
            order.put("nm_penerima", etNm.getText().toString().trim());
            order.put("email", etEmail.getText().toString().trim());
            order.put("alamat_kirim", etAlamat.getText().toString().trim());
            order.put("telp_kirim", etTelp.getText().toString().trim());
            order.put("kota", spKota.getSelectedItem().toString().trim());
            order.put("provinsi", spProvinsi.getSelectedItem().toString().trim());
            order.put("lamakirim", etLamaKirim.getText().toString().trim());
            order.put("kodepos", etKodePos.getText().toString().trim());
            order.put("metodebayar", metodeInt);
            order.put("subtotal", Integer.parseInt(etSubtotal.getText().toString()));
            order.put("ongkir", Integer.parseInt(etOngkir.getText().toString()));
            order.put("total_bayar", Integer.parseInt(etTotalBayar.getText().toString()));

            List<Produk> cartList = CartManager.getCartByUser(this);
            for (Produk produk : cartList) {
                JSONObject item = new JSONObject();
                item.put("kode", produk.getId_produk());
                item.put("harga", Integer.parseInt(produk.getHarga()));
                item.put("qty", produk.getQuantity());
                orderDetail.put(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                response -> {
                    Toast.makeText(this, "Checkout Berhasil", Toast.LENGTH_SHORT).show();

                    // ✅ Hapus cart
                    CartManager.clearCart(this);

                    // ✅ Redirect ke Main/Home
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order", order.toString());
                params.put("order_detail", orderDetail.toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
package com.example.utsmobile2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.utsmobile2.adapter.HistoryOrderAdapter;
import com.example.utsmobile2.model.HistoryOrder;
import com.example.utsmobile2.model.OrderDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryOrderActivity extends AppCompatActivity implements BuktiUploadCallback{
    String baseUrl = ApiClient.getBaseUrl();
    String URL_GET_HISTORY = baseUrl + "get_history_order.php";
    String URL_UPLOAD_BUKTI = baseUrl + "upload_bukti.php";
    Uri filePath;
    private RecyclerView rvHistory;
    private TextView tvEmpty;
    private static final int PICK_IMAGE_REQUEST = 1;
    public int currentTransId = -1;
    private Bitmap selectedBitmap;
    public String trans_id;
    String fileName = "";
    private List<HistoryOrder> historyList = new ArrayList<>();
    private HistoryOrderAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rvHistory = findViewById(R.id.rvHistory);
        tvEmpty = findViewById(R.id.tvEmpty);

        adapter = new HistoryOrderAdapter(this, historyList, new BuktiUploadCallback() {
            @Override
            public void onUploadClick(int transId) {
                currentTransId = transId;
                selectImageFromGallery();
            }

            @Override
            public void onUploadTrigger() {
                if (filePath != null) { // Ensure filePath is not null
                    try {
                        uploadBuktiBayar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(HistoryOrderActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email != null) {
            loadHistory(email);
        } else {
            Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            filePath = imageUri;
            fileName = getFileName(filePath);

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Preview di RecyclerView
            adapter.updateImage(currentTransId, imageUri);

            // ✅ Otomatis upload setelah pilih gambar
            try {
                uploadBuktiBayar();
            } catch (IOException e) {
                Toast.makeText(this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "Uploading: " + fileName, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onUploadClick(int transId) {
        currentTransId = transId;
        selectImageFromGallery();  // Atau simpan trans_id
    }
    @Override
    public void onUploadTrigger() {
        try {
            uploadBuktiBayar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }

    private void uploadBuktiBayar() throws IOException {
        InputStream iStream = getContentResolver().openInputStream(filePath);
        byte[] imageData = getBytes(iStream); // Sudah aman

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_UPLOAD_BUKTI,
                response -> {
                    Toast.makeText(this, "Upload Bukti Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, HistoryOrderActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Upload Gagal: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // ✅ Kirim orderid dengan benar
        Map<String, String> stringParams = new HashMap<>();
        stringParams.put("orderid", String.valueOf(currentTransId)); // PENTING!
        multipartRequest.setParams(stringParams);

        // ✅ Kirim file
        Map<String, VolleyMultipartRequest.DataPart> byteParams = new HashMap<>();
        byteParams.put("bukti", new VolleyMultipartRequest.DataPart(fileName, imageData, "image/jpeg"));
        multipartRequest.setByteData(byteParams);

        // ✅ Queue
        Volley.newRequestQueue(this).add(multipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void loadHistory(String email) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_HISTORY,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("kode") == 1) {
                            JSONArray arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject o = arr.getJSONObject(i);
                                HistoryOrder order = new HistoryOrder();
                                order.trans_id = o.getInt("trans_id");
                                order.tgl_order = o.getString("tgl_order");
                                order.total_bayar = o.getInt("total_bayar");
                                order.alamat_kirim = o.getString("alamat_kirim");
                                order.kota = o.getString("kota");
                                order.provinsi = o.getString("provinsi");
                                order.kodepos = o.getString("kodepos");
                                order.subtotal = o.getInt("subtotal");
                                order.ongkir = o.getInt("ongkir");
                                order.metodebayar = o.getInt("metodebayar");
                                order.lamakirim = o.getString("lamakirim");
                                order.status = o.getInt("status");
                                order.buktibayar = o.optString("buktibayar");
                                order.nm_penerima = o.getString("nm_penerima");

                                JSONArray detailArray = o.getJSONArray("detail");
                                List<OrderDetail> detailList = new ArrayList<>();
                                for (int j = 0; j < detailArray.length(); j++) {
                                    JSONObject d = detailArray.getJSONObject(j);
                                    OrderDetail det = new OrderDetail();
                                    det.kode_brg = d.getString("kode_brg");
                                    det.qty = d.getInt("qty");
                                    det.bayar = d.getInt("bayar");
                                    detailList.add(det);
                                }

                                order.detail = detailList;
                                historyList.add(order);
                            }

                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(historyList.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("email", email);
                return p;
            }
        };

        Volley.newRequestQueue(this).add(request);
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

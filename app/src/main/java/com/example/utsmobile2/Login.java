package com.example.utsmobile2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    Button btnLogin;
    ImageView btnBack;
    TextView btnRegist;
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences;

    public static final String PREF_NAME = "user_session";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBack = (ImageView) findViewById(R.id.loginBack);
        btnRegist = (TextView) findViewById(R.id.registLink);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,OnBoarding.class);
                startActivity(intent);
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Cek apakah user sudah login sebelumnya
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan")
                        .setContentText("Harap isi email dan password terlebih dahulu!")
                        .show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Email Tidak Valid")
                        .setContentText("Gunakan format email yang benar, seperti nama@domain.com")
                        .show();
            } else {
                SweetAlertDialog loadingDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#66CCFF"));
                loadingDialog.setTitleText("Memeriksa akun...");
                loadingDialog.setCancelable(false);
                loadingDialog.show();

                // Panggil loginUser
                loginUser(email, password, loadingDialog); // jangan lupa sesuaikan method-nya
            }
        });

    }

    private void loginUser(String email, String password, SweetAlertDialog loadingDialog) {
        String url = "http://172.20.10.8/android/get_login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    loadingDialog.dismissWithAnimation();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");

                        if (status == 1) {
                            JSONObject user = jsonObject.getJSONObject("user");

                            // Simpan ke SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("id_user", user.getString("id"));
                            editor.putString("nama", user.getString("nama"));
                            editor.putString("email", user.getString("email"));

                            // Tambahkan waktu login
                            String tanggalLogin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    .format(new Date());
                            editor.putString("last_login", tanggalLogin);
                            editor.apply();

                            // Dialog sukses login
                            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Login Berhasil!")
                                    .setContentText("Selamat datang, " + user.getString("nama"))
                                    .setConfirmClickListener(sDialog -> {
                                        sDialog.dismissWithAnimation();
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    })
                                    .show();

                        } else {
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Login Gagal")
                                    .setContentText(message)
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan")
                                .setContentText("Format data tidak valid!")
                                .show();
                    }
                },
                error -> {
                    loadingDialog.dismissWithAnimation();
                    error.printStackTrace();
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Koneksi Gagal")
                            .setContentText("Tidak dapat terhubung ke server.")
                            .show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}
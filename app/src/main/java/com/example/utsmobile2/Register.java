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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {
    ImageView btnBack;
    TextView btnLogin;
    EditText etNama, etEmail, etPassword;
    Button btnRegist;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etNama = (EditText) findViewById(R.id.etNama);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnRegist = (Button) findViewById(R.id.btnRegist);
        btnBack = (ImageView) findViewById(R.id.registBack);
        btnLogin = (TextView) findViewById(R.id.loginLink);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,OnBoarding.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        btnRegist.setOnClickListener(v -> {
            String newUser = etNama.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPass = etPassword.getText().toString().trim();

            if (newUser.isEmpty() || newEmail.isEmpty() || newPass.isEmpty()) {
                new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan")
                        .setContentText("Harap isi semua data terlebih dahulu!")
                        .show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Email Tidak Valid")
                        .setContentText("Masukkan alamat email yang benar, seperti nama@domain.com")
                        .show();
            } else {
                SweetAlertDialog loadingDialog = new SweetAlertDialog(Register.this, SweetAlertDialog.PROGRESS_TYPE);
                loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                loadingDialog.setTitleText("Mendaftarkan...");
                loadingDialog.setCancelable(false);
                loadingDialog.show();

                String url = "http://172.20.10.8/android/post_register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            loadingDialog.dismissWithAnimation();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                int result = jsonObject.getInt("result");
                                String message = jsonObject.getString("message");

                                if (status == 1 && result == 1) {
                                    new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Sukses!")
                                            .setContentText(message)
                                            .setConfirmClickListener(sDialog -> {
                                                sDialog.dismissWithAnimation();
                                                Intent intent = new Intent(Register.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .show();
                                } else {
                                    new SweetAlertDialog(Register.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Gagal")
                                            .setContentText(message)
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Kesalahan!")
                                        .setContentText("Format data error!")
                                        .show();
                            }
                        },
                        error -> {
                            loadingDialog.dismissWithAnimation();
                            error.printStackTrace();
                            new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Gagal!")
                                    .setContentText("Tidak dapat terhubung ke server.")
                                    .show();
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("nama", newUser);
                        params.put("email", newEmail);
                        params.put("password", newPass);
                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(stringRequest);
            }
        });

    }
}
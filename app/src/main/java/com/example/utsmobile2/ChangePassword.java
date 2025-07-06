package com.example.utsmobile2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    private EditText currentPassword, newPassword, confirmPassword;
    private Button changePasswordButton;
    private TextView message;
    private SharedPreferences sharedPreferences;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sharedPreferences = getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        message = findViewById(R.id.message);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPass = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPass)) {
            message.setText("Current password is required.");
            message.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(newPass)) {
            message.setText("New password is required.");
            message.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            message.setText("Please confirm your new password.");
            message.setVisibility(View.VISIBLE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            message.setText("New passwords do not match.");
            message.setVisibility(View.VISIBLE);
            return;
        }

        // Call the API to change the password
        String url = ApiClient.getBaseUrl() + "update_pw.php"; // Replace with your actual endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String messageResponse = jsonResponse.getString("message");

                            if (status == 1) {
                                // Use ChangePassword.this to get the correct context
                                Toast.makeText(ChangePassword.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity
                            } else {
                                message.setText(messageResponse);
                                message.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            message.setText("Error parsing response.");
                            message.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        message.setText("Error: " + error.getMessage());
                        message.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail); // Replace with the actual email
                params.put("current_password", currentPass);
                params.put("new_password", newPass);
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }
}

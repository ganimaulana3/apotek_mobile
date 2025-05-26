package com.example.utsmobile2.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.EditProfile;
import com.example.utsmobile2.KontakKami;
import com.example.utsmobile2.Login;
import com.example.utsmobile2.R;
import com.example.utsmobile2.ResponseUser;
import com.example.utsmobile2.User;
import com.example.utsmobile2.databinding.FragmentProfileBinding;
import com.google.android.material.button.MaterialButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);

        MaterialButton btnLogout = binding.btnLogout;
        MaterialButton btnEditProfile = binding.btnEditProfile;
        MaterialButton btnContact = binding.btnContact;

        btnLogout.setOnClickListener(v -> {
            new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Yakin Logout?")
                    .setContentText("Kamu akan keluar dari akun ini.")
                    .setConfirmText("Logout")
                    .setCancelText("Batal")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Logout Berhasil")
                                .setConfirmText("OK")
                                .setConfirmClickListener(doneDialog -> {
                                    doneDialog.dismissWithAnimation();
                                    startActivity(new Intent(requireActivity(), Login.class));
                                    requireActivity().finish();
                                })
                                .show();
                    })
                    .show();
        });

        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditProfile.class));
        });

        btnContact.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), KontakKami.class));
        });

        String idUser = sharedPreferences.getString("id_user", "");

        String url = ApiClient.getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);

        Call<ResponseUser> call = api.getUser(idUser);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body().getUser();
                    binding.tvNama.setText("Nama: " + user.getNama());
                    binding.tvAlamat.setText("Alamat: " + user.getAlamat());
                    binding.tvKota.setText("Kota: " + user.getKota());
                    binding.tvProvinsi.setText("Provinsi: " + user.getProvinsi());
                    binding.tvKodePos.setText("Kode Pos: " + user.getKodepos());
                    binding.tvEmail.setText("Email: " + user.getEmail());
                } else {
                    Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

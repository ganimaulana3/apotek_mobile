package com.example.utsmobile2.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.ChangePassword;
import com.example.utsmobile2.EditProfile;
import com.example.utsmobile2.HistoryOrderActivity;
import com.example.utsmobile2.KontakKami;
import com.example.utsmobile2.Login;
import com.example.utsmobile2.R;
import com.example.utsmobile2.ResponseUser;
import com.example.utsmobile2.User;
import com.example.utsmobile2.databinding.FragmentProfileBinding;
import com.example.utsmobile2.model.HistoryOrder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private TextView txtLocation;
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
        txtLocation = binding.txtProfileLocation;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getCurrentLocation();

        TextView btnLogout = binding.itemLogout;
        TextView btnEditProfile = binding.itemEditProfile;
        TextView btnContact = binding.itemHelpCenter;
        TextView btnOrderH = binding.itemMyOrders;
        TextView btnPassword = binding.itemChangeP;
        ImageView pp = binding.imgProfile;

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
        btnPassword.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), ChangePassword.class));
        });

        btnContact.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), KontakKami.class));
        });

        btnOrderH.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), HistoryOrderActivity.class));
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
                    binding.txtProfileName.setText("Nama: " + user.getNama());
//                    binding.tvAlamat.setText("Alamat: " + user.getAlamat());
//                    binding.tvKota.setText("Kota: " + user.getKota());
//                    binding.tvProvinsi.setText("Provinsi: " + user.getProvinsi());
//                    binding.tvKodePos.setText("Kode Pos: " + user.getKodepos());
                    binding.txtProfileEmail.setText("Email: " + user.getEmail());
                    Glide.with(requireContext())
                            .load(ApiClient.getBaseUrl() + "profile/" + user.getFoto())
                            .placeholder(R.drawable.baseline_person_24)
                            .transform(new RoundedCorners(24))
                            .into(pp);
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

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }

        // Setup LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update setiap 5 detik
        locationRequest.setFastestInterval(2000); // Minimal 2 detik

        // Callback saat lokasi tersedia
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    txtLocation.setText("Lokasi tidak tersedia");
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Konversi koordinat ke alamat
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),
                                location.getLongitude(),
                                1
                        );
                        if (addresses != null && !addresses.isEmpty()) {
                            String address = addresses.get(0).getAddressLine(0);
                            txtLocation.setText(address);
                        } else {
                            txtLocation.setText("Alamat tidak ditemukan");
                        }
                    } catch (IOException e) {
                        txtLocation.setText("Gagal mendapatkan alamat");
                        e.printStackTrace();
                    }

                    // Stop setelah dapat lokasi pertama
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        };

        // Mulai permintaan lokasi
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        binding = null;
    }
}

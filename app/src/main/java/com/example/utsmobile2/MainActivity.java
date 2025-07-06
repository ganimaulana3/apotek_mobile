package com.example.utsmobile2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.utsmobile2.databinding.ActivityMainBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_produk, R.id.navigation_keranjang, R.id.navigation_profile, R.id.navigation_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_profile) {
                SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if (!isLoggedIn) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Anda belum login")
                            .setContentText("Silakan login terlebih dahulu.")
                            .setConfirmText("Login")
                            .setCancelText("Batal")
                            .setConfirmClickListener(sDialog -> {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                            })
                            .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                            .show();

                    return false; // Jangan load fragment
                }
            }

            // Jika login atau bukan menu profile, lanjutkan ke fragment
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });


    }

}
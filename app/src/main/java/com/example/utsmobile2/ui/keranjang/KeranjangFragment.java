package com.example.utsmobile2.ui.keranjang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsmobile2.CartAdapter;
import com.example.utsmobile2.CartManager;
import com.example.utsmobile2.Login;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.R;
import com.example.utsmobile2.databinding.FragmentKeranjangBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KeranjangFragment extends Fragment {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private List<Produk> cartList;
    TextView txtTotalHarga, txtKosong;
    Button btnCheckout;
    private FragmentKeranjangBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KeranjangViewModel keranjangViewModel =
                new ViewModelProvider(this).get(KeranjangViewModel.class);

        binding = FragmentKeranjangBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rvCart = root.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ Get full cart (supports guest too)
        cartList = CartManager.getCart(getContext());

        cartAdapter = new CartAdapter(getContext(), cartList, produk -> {
            CartManager.removeFromCart(getContext(), produk);
            refreshCart(); // update tampilan
        });

        rvCart.setAdapter(cartAdapter);

        txtTotalHarga = root.findViewById(R.id.totalCart);
        txtKosong = root.findViewById(R.id.txtKosong);
        updateTotalHarga();
        checkCartKosong();

        btnCheckout = root.findViewById(R.id.checkout);

        SharedPreferences preferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if (!isLoggedIn) {
                    // 🔔 Show alert and redirect to Login
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Login Diperlukan")
                            .setContentText("Silahkan login terlebih dahulu untuk melanjutkan checkout.")
                            .setConfirmText("Login Sekarang")
                            .setCancelText("Batal")
                            .setConfirmClickListener(sDialog -> {
                                sDialog.dismissWithAnimation();

                                // 🚀 Go to LoginActivity
                                Intent intent = new Intent(getContext(), Login.class);
                                startActivity(intent);
                            })
                            .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                            .show();
                } else {
                    // ✅ User is logged in, continue to Checkout
//                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
//                    startActivity(intent);
                }
            }
        });


        return root;
    }

    private void refreshCart() {
        cartList.clear();
        cartList.addAll(CartManager.getCart(getContext())); // ✅ includes guest items
        cartAdapter.notifyDataSetChanged();
        updateTotalHarga();
        checkCartKosong();
    }

    private void updateTotalHarga() {
        int total = 0;
        for (Produk p : cartList) {
            try {
                int harga = Integer.parseInt(p.getHarga());
                total += harga * p.getQuantity();
            } catch (NumberFormatException e) {
                // Defensive programming 💡
                e.printStackTrace();
            }
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        txtTotalHarga.setText("Total: Rp " + formatter.format(total));
    }

    private void checkCartKosong() {
        if (cartList.isEmpty()) {
            txtKosong.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            txtTotalHarga.setVisibility(View.GONE);
            txtKosong.setText("Keranjang kamu masih kosong.");
        } else {
            txtKosong.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            txtTotalHarga.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

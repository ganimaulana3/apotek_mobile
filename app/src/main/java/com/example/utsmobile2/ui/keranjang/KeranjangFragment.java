package com.example.utsmobile2.ui.keranjang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsmobile2.CartAdapter;
import com.example.utsmobile2.CartManager;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.R;
import com.example.utsmobile2.databinding.FragmentKeranjangBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KeranjangFragment extends Fragment {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private List<Produk> cartList;
    TextView txtTotalHarga, txtKosong;
    private FragmentKeranjangBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        KeranjangViewModel keranjangViewModel =
                new ViewModelProvider(this).get(KeranjangViewModel.class);

        binding = FragmentKeranjangBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rvCart = root.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        cartList = CartManager.getCartByUser(getContext());// ambil dari SharedPreferences
        cartAdapter = new CartAdapter(getContext(), cartList, produk -> {
            CartManager.removeFromCart(getContext(), produk);
            refreshCart(); // update tampilan
        });

        rvCart.setAdapter(cartAdapter);

        txtTotalHarga = root.findViewById(R.id.totalCart);
        txtKosong = root.findViewById(R.id.txtKosong); // Tambahkan ini
        updateTotalHarga(); // panggil fungsi ini untuk pertama kali
        checkCartKosong();

        return root;
    }

    private void refreshCart() {
        cartList.clear();
        cartList.addAll(CartManager.getCartByUser(getContext())); // atau getCart() kalau belum pakai filter user
        cartAdapter.notifyDataSetChanged(); // hanya refresh data, tidak bikin adapter baru
        updateTotalHarga(); // update total harga
        checkCartKosong();
    }

    private void updateTotalHarga() {
        int total = 0;
        for (Produk p : cartList) {
            int harga = Integer.parseInt(p.getHarga());
            total += harga * p.getQuantity();
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        txtTotalHarga.setText("Total: Rp " + formatter.format(total));
    }

    private void checkCartKosong() {
        if (cartList.isEmpty()) {
            txtKosong.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            txtTotalHarga.setVisibility(View.GONE);
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
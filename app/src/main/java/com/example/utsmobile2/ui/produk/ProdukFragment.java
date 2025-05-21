package com.example.utsmobile2.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.ProdukAdapter;
import com.example.utsmobile2.R;
import com.example.utsmobile2.databinding.FragmentProdukBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukFragment extends Fragment {
    RecyclerView rvProducts;
    ProdukAdapter adapter;
    SearchView searchView;
    TextView txtNoResult;
    Button btnDetail;
    List<Produk> produkList = new ArrayList<>();

    private FragmentProdukBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProdukViewModel produkViewModel =
                new ViewModelProvider(this).get(ProdukViewModel.class);

        binding = FragmentProdukBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNoResult = root.findViewById(R.id.txtNoResult);

        rvProducts = root.findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProduk().enqueue(new Callback<List<Produk>>() {
            @Override
            public void onResponse(Call<List<Produk>> call, Response<List<Produk>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    produkList = response.body();
                    adapter = new ProdukAdapter(getContext(), produkList, txtNoResult);
                    rvProducts.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Produk>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });

        searchView = root.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Tidak perlu submit, filter saat mengetik
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });



        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHint("Cari produk...");
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.grey));
        searchEditText.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
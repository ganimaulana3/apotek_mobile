package com.example.utsmobile2.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.Login;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.ProdukAdapter;
import com.example.utsmobile2.R;
import com.example.utsmobile2.ResponseUser;
import com.example.utsmobile2.User;
import com.example.utsmobile2.databinding.FragmentHomeBinding;
import com.example.utsmobile2.ui.home.kategori.Kategori1;
import com.example.utsmobile2.ui.home.kategori.Kategori2;
import com.example.utsmobile2.ui.home.kategori.Kategori3;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    TextView nmDisplay;
    Context context;
    ImageView pp;
    Button btnRekomendasi, btnTerlaris, btnAdidas, btnPuma;
    private FragmentHomeBinding binding;
    private HomeViewModel profileViewModel;
    private SharedPreferences sharedPreferences;
    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPreferences = requireContext().getSharedPreferences(Login.PREF_NAME, Context.MODE_PRIVATE);
        String nmUser = sharedPreferences.getString("nama", "");
        String idUser = sharedPreferences.getString("id_user", "");
        nmDisplay = root.findViewById(R.id.user_name);
        nmDisplay.setText(String.format("Welcome %s", nmUser));
        context = getContext();

        // Initialize buttons
        btnRekomendasi = root.findViewById(R.id.btn_rekomendasi);
        btnTerlaris = root.findViewById(R.id.btn_terlaris);

        MaterialButton btnKategori1 = root.findViewById(R.id.category1);
        btnKategori1.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), Kategori1.class));
        });
        MaterialButton btnKategori2 = root.findViewById(R.id.category2);
        btnKategori2.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), Kategori2.class));
        });
        MaterialButton btnKategori3 = root.findViewById(R.id.category3);
        btnKategori3.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), Kategori3.class));
        });

        pp = root.findViewById(R.id.user_avatar);
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

//        btnAdidas = root.findViewById(R.id.btn_adidas);
//        btnPuma = root.findViewById(R.id.btn_puma);

        ImageSlider imageSlider = root.findViewById(R.id.banner_image);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slide1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide4, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Set listeners
        setupCategoryListeners();
        loadCategoryFragment(new RekomendasiFragment());


//        final TextView textView = binding.textProfile;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    private void setupCategoryListeners() {
        btnRekomendasi.setOnClickListener(v -> {
            updateCategoryUI("rekomendasi");
            loadCategoryFragment(new RekomendasiFragment());
        });

        btnTerlaris.setOnClickListener(v -> {
            updateCategoryUI("terlaris");
            loadCategoryFragment(new TerlarisFragment());
        });
//
//        btnAdidas.setOnClickListener(v -> {
//            updateCategoryUI("adidas");
//            loadCategoryFragment(new AdidasFragment());
//        });
//
//        btnPuma.setOnClickListener(v -> {
//            updateCategoryUI("puma");
//            loadCategoryFragment(new PumaFragment());
//        });
    }

    private void loadCategoryFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.category_container, fragment)
                .commit();
    }

    private void updateCategoryUI(String selected) {
        int selectedColor = ContextCompat.getColor(context, R.color.primary);
        int defaultColor = ContextCompat.getColor(context, R.color.grey);

        updateButtonState(btnRekomendasi, selected.equals("rekomendasi"), selectedColor, defaultColor);
        updateButtonState(btnTerlaris, selected.equals("terlaris"), selectedColor, defaultColor);
//        updateButtonState(btnAdidas, selected.equals("adidas"), selectedColor, defaultColor);
//        updateButtonState(btnPuma, selected.equals("puma"), selectedColor, defaultColor);
    }

    private void updateButtonState(Button button, boolean isSelected, int selectedColor, int defaultColor) {
        button.setBackgroundTintList(ColorStateList.valueOf(isSelected ? selectedColor : defaultColor));
        button.setTextColor(isSelected ? Color.WHITE : Color.BLACK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

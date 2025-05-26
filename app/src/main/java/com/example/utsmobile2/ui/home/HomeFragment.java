package com.example.utsmobile2.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.utsmobile2.ApiClient;
import com.example.utsmobile2.ApiService;
import com.example.utsmobile2.Produk;
import com.example.utsmobile2.ProdukAdapter;
import com.example.utsmobile2.R;
import com.example.utsmobile2.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    Context context;
    Button btnRekomendasi, btnTerlaris, btnAdidas, btnPuma;
    private FragmentHomeBinding binding;
    private HomeViewModel profileViewModel;

    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();

        // Initialize buttons
        btnRekomendasi = root.findViewById(R.id.btn_rekomendasi);
        btnTerlaris = root.findViewById(R.id.btn_terlaris);
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

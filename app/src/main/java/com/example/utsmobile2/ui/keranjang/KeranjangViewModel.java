package com.example.utsmobile2.ui.keranjang;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KeranjangViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public KeranjangViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Keranjang fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.chillcup02_ui.ui.catalog;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chillcup02_ui.data.repository.ProductRepository;
import com.example.chillcup02_ui.domain.model.Product;

import java.util.List;

public class CatalogViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        this.productRepository = new ProductRepository(application.getApplicationContext());
    }

    public LiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    public void loadProducts() {
        List<Product> products = productRepository.getProducts();
        productsLiveData.postValue(products);
    }
}

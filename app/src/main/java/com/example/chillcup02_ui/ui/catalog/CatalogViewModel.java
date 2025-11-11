package com.example.chillcup02_ui.ui.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.usecase.LoadCatalogUseCase;

import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final LoadCatalogUseCase loadCatalogUseCase;
    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> _products = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loadingCategories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loadingProducts = new MutableLiveData<>();

    private String selectedCategoryId = null; // null means show all products

    public CatalogViewModel() {
        CatalogRepository repository = new CatalogRepository();
        this.loadCatalogUseCase = new LoadCatalogUseCase(repository);
    }

    public LiveData<List<Category>> getCategories() {
        return _categories;
    }

    public LiveData<List<Product>> getProducts() {
        return _products;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public LiveData<Boolean> getLoadingCategories() {
        return _loadingCategories;
    }

    public LiveData<Boolean> getLoadingProducts() {
        return _loadingProducts;
    }

    public void loadCategories() {
        _loadingCategories.setValue(true);
        _error.setValue(null);

        loadCatalogUseCase.loadCategories(new CatalogRepository.ResultCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {
                _loadingCategories.setValue(false);
                _categories.setValue(categories);
            }

            @Override
            public void onError(String message) {
                _loadingCategories.setValue(false);
                _error.setValue(message);
            }
        });
    }

    public void loadProducts() {
        _loadingProducts.setValue(true);
        _error.setValue(null);

        if (selectedCategoryId == null) {
            // Load all products
        loadCatalogUseCase.loadAllProducts(new CatalogRepository.ResultCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                android.util.Log.d("CatalogViewModel", "Products loaded successfully: " + (products != null ? products.size() : "null"));
                _loadingProducts.setValue(false);
                _products.setValue(products);
            }

            @Override
            public void onError(String message) {
                android.util.Log.e("CatalogViewModel", "Error loading products: " + message);
                _loadingProducts.setValue(false);
                _error.setValue(message);
            }
        });
        } else {
            // Load products by category
            loadCatalogUseCase.loadProductsByCategory(selectedCategoryId, new CatalogRepository.ResultCallback<List<Product>>() {
                @Override
                public void onSuccess(List<Product> products) {
                    _loadingProducts.setValue(false);
                    _products.setValue(products);
                }

                @Override
                public void onError(String message) {
                    _loadingProducts.setValue(false);
                    _error.setValue(message);
                }
            });
        }
    }

    public void selectCategory(String categoryId) {
        this.selectedCategoryId = categoryId;
        loadProducts();
    }

    public void showAllProducts() {
        this.selectedCategoryId = null;
        loadProducts();
    }

    public String getSelectedCategoryId() {
        return selectedCategoryId;
    }
}

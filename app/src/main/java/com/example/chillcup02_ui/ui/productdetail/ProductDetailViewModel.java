package com.example.chillcup02_ui.ui.productdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.ServiceLocator;
import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.data.repository.ToggleResult;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;
import com.example.chillcup02_ui.domain.usecase.ToggleFavouriteUseCase;

import java.util.List;

public class ProductDetailViewModel extends ViewModel {

    private final CatalogRepository repository;
    private final MutableLiveData<Product> _product = new MutableLiveData<>();
    private final MutableLiveData<List<Size>> _sizes = new MutableLiveData<>();
    private final MutableLiveData<List<Topping>> _toppings = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>();

    public ProductDetailViewModel() {
        this.repository = new CatalogRepository();
    }

    public LiveData<Product> getProduct() {
        return _product;
    }

    public LiveData<List<Size>> getSizes() {
        return _sizes;
    }

    public LiveData<List<Topping>> getToppings() {
        return _toppings;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public void loadProduct(String productId) {
        _loading.setValue(true);
        _error.setValue(null);

        repository.getProductById(productId, new CatalogRepository.ResultCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                _loading.setValue(false);
                _product.setValue(product);
            }

            @Override
            public void onError(String message) {
                _loading.setValue(false);
                _error.setValue(message);
            }
        });
    }

    public void loadSizes() {
        repository.getAllSizes(new CatalogRepository.ResultCallback<List<Size>>() {
            @Override
            public void onSuccess(List<Size> sizes) {
                _sizes.setValue(sizes);
            }

            @Override
            public void onError(String message) {
                _error.setValue("Failed to load sizes: " + message);
            }
        });
    }

    public void loadToppings() {
        repository.getAllToppings(new CatalogRepository.ResultCallback<List<Topping>>() {
            @Override
            public void onSuccess(List<Topping> toppings) {
                _toppings.setValue(toppings);
            }

            @Override
            public void onError(String message) {
                _error.setValue("Failed to load toppings: " + message);
            }
        });
    }

    public void toggleFavourite(String productId) {
        ToggleFavouriteUseCase toggleFavouriteUseCase = ServiceLocator.getToggleFavouriteUseCase();
        toggleFavouriteUseCase.execute(productId, new ToggleFavouriteUseCase.UseCaseCallback<ToggleResult>() {
            @Override
            public void onSuccess(ToggleResult result) {
                // Update UI based on result status
                String message = "added".equals(result.getStatus()) ?
                    "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
                _error.setValue(message); // Using error LiveData for success messages temporarily
            }

            @Override
            public void onError(String error) {
                _error.setValue("Lỗi: " + error);
            }
        });
    }
}

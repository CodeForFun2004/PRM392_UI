package com.example.chillcup02_ui.ui.productdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.ServiceLocator;
import com.example.chillcup02_ui.data.repository.CatalogRepository;
import com.example.chillcup02_ui.data.repository.ToggleResult;
import com.example.chillcup02_ui.domain.model.Favourite;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;
import com.example.chillcup02_ui.domain.usecase.LoadFavouritesUseCase;
import com.example.chillcup02_ui.domain.usecase.ToggleFavouriteUseCase;

import java.util.List;

public class ProductDetailViewModel extends ViewModel {

    private final CatalogRepository repository;
    private final MutableLiveData<Product> _product = new MutableLiveData<>();
    private final MutableLiveData<List<Size>> _sizes = new MutableLiveData<>();
    private final MutableLiveData<List<Topping>> _toppings = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>();
    private final MutableLiveData<String> _favouriteMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isFavourite = new MutableLiveData<>(false); // Default to false

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

    public LiveData<String> getFavouriteMessage() {
        return _favouriteMessage;
    }

    public LiveData<Boolean> getIsFavourite() {
        return _isFavourite;
    }

    public void loadProduct(String productId) {
        _loading.setValue(true);
        _error.setValue(null);

        repository.getProductById(productId, new CatalogRepository.ResultCallback<Product>() {
            @Override
            public void onSuccess(Product product) {
                _loading.setValue(false);
                _product.setValue(product);
                // Check favourite status after product is loaded
                checkFavouriteStatus(productId);
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
                // Update favorite status and show toast message
                boolean isFavourite = "added".equals(result.getStatus());
                _isFavourite.setValue(isFavourite);

                String message = isFavourite ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
                _favouriteMessage.setValue(message);
            }

            @Override
            public void onError(String error) {
                _error.setValue("Lỗi: " + error);
            }
        });
    }

    public void checkFavouriteStatus(String productId) {
        android.util.Log.d("ProductDetailViewModel", "checkFavouriteStatus called for productId: " + productId);
        // Check if product is favourited by calling the API
        LoadFavouritesUseCase loadFavouritesUseCase = ServiceLocator.getLoadFavouritesUseCase();
        loadFavouritesUseCase.execute(new LoadFavouritesUseCase.UseCaseCallback<List<Favourite>>() {
            @Override
            public void onSuccess(List<Favourite> favourites) {
                android.util.Log.d("ProductDetailViewModel", "checkFavouriteStatus onSuccess, favourites count: " + (favourites != null ? favourites.size() : 0));
                // Check if the current product is in favourites
                boolean isFavourite = false;
                if (favourites != null) {
                    for (Favourite favourite : favourites) {
                        android.util.Log.d("ProductDetailViewModel", "Checking favourite product: " + (favourite.getProduct() != null ? favourite.getProduct().getId() : "null"));
                        if (favourite.getProduct() != null &&
                            productId.equals(favourite.getProduct().getId())) {
                            isFavourite = true;
                            android.util.Log.d("ProductDetailViewModel", "Product " + productId + " is favourited!");
                            break;
                        }
                    }
                }
                android.util.Log.d("ProductDetailViewModel", "Setting isFavourite to: " + isFavourite);
                _isFavourite.setValue(isFavourite);
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("ProductDetailViewModel", "checkFavouriteStatus onError: " + error);
                // On error, set favourite status to false as fallback
                // This ensures the UI shows the correct state even if API fails
                _isFavourite.setValue(false);
            }
        });
    }
}

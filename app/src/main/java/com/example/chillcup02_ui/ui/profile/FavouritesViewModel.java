package com.example.chillcup02_ui.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.api.ApiClient;
import com.example.chillcup02_ui.data.repository.FavouriteRepository;
import com.example.chillcup02_ui.domain.model.Favourite;
import com.example.chillcup02_ui.domain.usecase.LoadFavouritesUseCase;

import java.util.List;

public class FavouritesViewModel extends ViewModel {

    private final LoadFavouritesUseCase loadFavouritesUseCase;
    private final MutableLiveData<List<Favourite>> _favourites = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>();

    public FavouritesViewModel() {
        FavouriteRepository repository = new FavouriteRepository(ApiClient.getApiService());
        this.loadFavouritesUseCase = new LoadFavouritesUseCase(repository);
    }

    public LiveData<List<Favourite>> getFavourites() {
        return _favourites;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public LiveData<Boolean> getLoading() {
        return _loading;
    }

    public void loadFavourites() {
        _loading.setValue(true);
        _error.setValue(null);

        loadFavouritesUseCase.execute(new LoadFavouritesUseCase.UseCaseCallback<List<Favourite>>() {
            @Override
            public void onSuccess(List<Favourite> favourites) {
                _loading.setValue(false);
                _favourites.setValue(favourites);
            }

            @Override
            public void onError(String error) {
                _loading.setValue(false);
                _error.setValue(error);
            }
        });
    }
}

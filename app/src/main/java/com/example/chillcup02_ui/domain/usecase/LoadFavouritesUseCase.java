package com.example.chillcup02_ui.domain.usecase;

import com.example.chillcup02_ui.data.repository.FavouriteRepository;
import com.example.chillcup02_ui.domain.model.Favourite;

import java.util.List;

public class LoadFavouritesUseCase {

    private final FavouriteRepository favouriteRepository;

    public LoadFavouritesUseCase(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public void execute(UseCaseCallback<List<Favourite>> callback) {
        favouriteRepository.getMyFavourites(new FavouriteRepository.RepositoryCallback<List<Favourite>>() {
            @Override
            public void onSuccess(List<Favourite> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public interface UseCaseCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

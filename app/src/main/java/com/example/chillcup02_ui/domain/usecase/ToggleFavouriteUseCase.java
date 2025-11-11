package com.example.chillcup02_ui.domain.usecase;

import com.example.chillcup02_ui.data.repository.FavouriteRepository;
import com.example.chillcup02_ui.data.repository.ToggleResult;

public class ToggleFavouriteUseCase {

    private final FavouriteRepository favouriteRepository;

    public ToggleFavouriteUseCase(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    public void execute(String productId, UseCaseCallback<ToggleResult> callback) {
        favouriteRepository.toggleFavourite(productId, new FavouriteRepository.RepositoryCallback<ToggleResult>() {
            @Override
            public void onSuccess(ToggleResult data) {
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

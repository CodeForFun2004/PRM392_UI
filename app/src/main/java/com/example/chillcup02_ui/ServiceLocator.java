package com.example.chillcup02_ui;

import com.example.chillcup02_ui.data.api.ApiClient;
import com.example.chillcup02_ui.data.repository.FavouriteRepository;
import com.example.chillcup02_ui.domain.usecase.LoadFavouritesUseCase;
import com.example.chillcup02_ui.domain.usecase.ToggleFavouriteUseCase;

public class ServiceLocator {

    private static ToggleFavouriteUseCase toggleFavouriteUseCase;
    private static LoadFavouritesUseCase loadFavouritesUseCase;

    public static ToggleFavouriteUseCase getToggleFavouriteUseCase() {
        if (toggleFavouriteUseCase == null) {
            FavouriteRepository favouriteRepository = new FavouriteRepository(ApiClient.getApiService());
            toggleFavouriteUseCase = new ToggleFavouriteUseCase(favouriteRepository);
        }
        return toggleFavouriteUseCase;
    }

    public static LoadFavouritesUseCase getLoadFavouritesUseCase() {
        if (loadFavouritesUseCase == null) {
            FavouriteRepository favouriteRepository = new FavouriteRepository(ApiClient.getApiService());
            loadFavouritesUseCase = new LoadFavouritesUseCase(favouriteRepository);
        }
        return loadFavouritesUseCase;
    }
}

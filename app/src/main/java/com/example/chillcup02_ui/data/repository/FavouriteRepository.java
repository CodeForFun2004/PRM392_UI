package com.example.chillcup02_ui.data.repository;

import com.example.chillcup02_ui.data.api.ApiService;
import com.example.chillcup02_ui.data.dto.FavouriteDto;
import com.example.chillcup02_ui.data.mapper.FavouriteMapper;
import com.example.chillcup02_ui.domain.model.Favourite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRepository {

    private final ApiService apiService;

    public FavouriteRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void addFavourite(String productId, RepositoryCallback<Favourite> callback) {
        Map<String, String> request = new HashMap<>();
        request.put("productId", productId);

        apiService.addFavourite(request).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // For now, create a mock favourite since the response doesn't contain full data
                    Favourite favourite = new Favourite();
                    favourite.setProduct(null); // Will be populated when fetching favourites
                    callback.onSuccess(favourite);
                } else {
                    callback.onError("Failed to add favourite");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getMyFavourites(RepositoryCallback<List<Favourite>> callback) {
        apiService.getMyFavourites().enqueue(new Callback<FavouriteDto.FavouritesResponse>() {
            @Override
            public void onResponse(Call<FavouriteDto.FavouritesResponse> call, Response<FavouriteDto.FavouritesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FavouriteDto> favouriteDtos = response.body().getFavourites();
                    List<Favourite> favourites = FavouriteMapper.toDomainList(favouriteDtos);
                    callback.onSuccess(favourites);
                } else {
                    callback.onError("Failed to load favourites");
                }
            }

            @Override
            public void onFailure(Call<FavouriteDto.FavouritesResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void removeFavourite(String productId, RepositoryCallback<Void> callback) {
        apiService.removeFavourite(productId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Failed to remove favourite");
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void toggleFavourite(String productId, RepositoryCallback<ToggleResult> callback) {
        Map<String, String> request = new HashMap<>();
        request.put("productId", productId);

        apiService.toggleFavourite(request).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> responseBody = response.body();
                    String status = (String) responseBody.get("status");

                    Favourite favourite = null;
                    if ("added".equals(status) && responseBody.containsKey("favourite")) {
                        // For added status, we have a favourite object
                        // Note: The backend returns the favourite, but for simplicity we'll create a basic one
                        favourite = new Favourite();
                        favourite.setProduct(null); // Will be populated when fetching favourites
                    }

                    ToggleResult result = new ToggleResult(favourite, status);
                    callback.onSuccess(result);
                } else {
                    callback.onError("Failed to toggle favourite");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }
}

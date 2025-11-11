package com.example.chillcup02_ui.data.repository;

import com.example.chillcup02_ui.data.api.ApiClient;
import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.mapper.CategoryMapper;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.util.Result;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogRepository {

    public void getCategories(ResultCallback<List<Category>> callback) {
        ApiClient.getApiService().getAllCategories().enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = CategoryMapper.toDomainList(response.body());
                    callback.onSuccess(categories);
                } else {
                    callback.onError("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface ResultCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }
}

package com.example.chillcup02_ui.data.api;

import com.example.chillcup02_ui.data.dto.CategoryDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/api/categories")
    Call<List<CategoryDto>> getAllCategories();
}

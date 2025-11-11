package com.example.chillcup02_ui.data.api;

import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.data.dto.ProductFilterResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/categories")
    Call<List<CategoryDto>> getAllCategories();

    @GET("/api/products")
    Call<List<ProductDto>> getAllProducts();

    @GET("/api/products/filter-by-category")
    Call<ProductFilterResponse> getProductsByCategory(@Query("categoryId") String categoryId);
}

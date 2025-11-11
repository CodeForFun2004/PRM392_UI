package com.example.chillcup02_ui.data.api;

import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.dto.LoginRequest;
import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.data.dto.ProductFilterResponse;
import com.example.chillcup02_ui.data.dto.RegisterRequest;
import com.example.chillcup02_ui.data.dto.RegisterResponse;
import com.example.chillcup02_ui.data.dto.VerifyOtpRequest;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Authentication endpoints
    @POST("/api/auth/register-request")
    Call<RegisterResponse> registerRequest(@Body RegisterRequest request);

    @POST("/api/auth/verify-register")
    Call<AuthResponse> verifyRegister(@Body VerifyOtpRequest request);

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/api/auth/refresh")
    Call<Map<String, String>> refreshToken(@Body Map<String, String> refreshTokenRequest);

    @POST("/api/auth/logout")
    Call<Map<String, String>> logout();

    // Product endpoints
    @GET("/api/categories")
    Call<List<CategoryDto>> getAllCategories();

    @GET("/api/products")
    Call<List<ProductDto>> getAllProducts();

    @GET("/api/products/filter-by-category")
    Call<ProductFilterResponse> getProductsByCategory(@Query("categoryId") String categoryId);

    @GET("/api/products/{id}")
    Call<ProductDto> getProductById(@retrofit2.http.Path("id") String id);

    @GET("/api/sizes")
    Call<List<Size>> getAllSizes();

    @GET("/api/toppings")
    Call<List<Topping>> getAllToppings();
}

package com.example.chillcup02_ui.data.api;

import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.dto.FavouriteDto;
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
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    // Firebase Auth endpoints (for new users)
    @POST("/api/firebase-auth/register")
    Call<Map<String, Object>> firebaseRegister(@Body Map<String, String> request);

    @POST("/api/firebase-auth/login")
    Call<Map<String, Object>> firebaseLogin(@Body Map<String, String> request);

    @POST("/api/firebase-auth/google-login")
    Call<Map<String, Object>> firebaseGoogleLogin(@Body Map<String, String> request);

    @POST("/api/firebase-auth/logout")
    Call<Map<String, String>> firebaseLogout();

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

    // Favourite endpoints
    @POST("/api/favourites")
    Call<Map<String, Object>> addFavourite(@Body Map<String, String> request);

    @GET("/api/favourites")
    Call<FavouriteDto.FavouritesResponse> getMyFavourites();

    @DELETE("/api/favourites/{productId}")
    Call<Map<String, String>> removeFavourite(@Path("productId") String productId);

    @POST("/api/favourites/toggle")
    Call<Map<String, Object>> toggleFavourite(@Body Map<String, String> request);
}

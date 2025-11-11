package com.example.chillcup02_ui.data.api;

import com.example.chillcup02_ui.data.dto.OrderDto;
import com.example.chillcup02_ui.data.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {

    // This is for fetching the user's own profile
    @GET("api/me")
    Call<UserDto> getMyProfile(@Header("Authorization") String authToken);

    // This is for fetching orders for a staff member's store
    @GET("api/orders/store/{storeId}")
    Call<List<OrderDto>> getOrdersForStore(@Path("storeId") String storeId);

}

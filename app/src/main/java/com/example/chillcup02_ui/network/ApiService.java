package com.example.chillcup02_ui.network;

import com.example.chillcup02_ui.network.model.OrderRequest;
import com.example.chillcup02_ui.network.model.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/mock/orders")
    Call<OrderResponse> createOrder(@Body OrderRequest body);

    @GET("api/mock/orders/{id}")
    Call<OrderResponse> getOrder(@Path("id") String id);

    @GET("api/mock/orders")
    Call<java.util.List<OrderResponse.Order>> listOrders();
}

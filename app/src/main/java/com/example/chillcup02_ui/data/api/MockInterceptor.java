package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    private static final String TAG = "MockInterceptor";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // In-memory storage for mock favourites
    private final Set<String> favouritedProducts = new HashSet<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();

        Log.d(TAG, "Intercepting request: " + request.method() + " " + url);

        // Skip favourite endpoints - let them go to real API
        if (url.contains("/api/favourites")) {
            Log.d(TAG, "Skipping mock for favourites - using real API");
            return chain.proceed(request);
        }

        // For other requests, proceed normally (they might fail if no server is running)
        return chain.proceed(request);
    }

    private Response handleFavouriteRequest(Request request) throws IOException {
        String method = request.method();
        String url = request.url().toString();

        Log.d(TAG, "Handling favourite request: " + method + " " + url);

        try {
            switch (method) {
                case "POST":
                    if (url.contains("/api/favourites/toggle")) {
                        return handleToggleFavourite(request);
                    } else if (url.contains("/api/favourites")) {
                        return handleAddFavourite(request);
                    }
                    break;
                case "GET":
                    if (url.contains("/api/favourites")) {
                        return handleGetFavourites(request);
                    }
                    break;
                case "DELETE":
                    return handleRemoveFavourite(request);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling favourite request", e);
        }

        // Return error response for unhandled requests
        return createErrorResponse(request, "Endpoint not implemented");
    }

    private Response handleToggleFavourite(Request request) throws Exception {
        // Simulate network delay
        Thread.sleep(500);

        // Parse request body to get productId
        String requestBody = getRequestBody(request);
        JSONObject jsonBody = new JSONObject(requestBody);
        String productId = jsonBody.optString("productId");

        if (productId == null || productId.isEmpty()) {
            return createErrorResponse(request, "Product ID is required");
        }

        // Toggle favourite status
        boolean wasFavourited = favouritedProducts.contains(productId);
        String status;

        if (wasFavourited) {
            favouritedProducts.remove(productId);
            status = "removed";
        } else {
            favouritedProducts.add(productId);
            status = "added";
        }

        // Create response
        JSONObject responseJson = new JSONObject();
        responseJson.put("status", status);
        responseJson.put("productId", productId);
        responseJson.put("success", true);

        Log.d(TAG, "Toggle favourite: " + productId + " -> " + status);

        return createSuccessResponse(request, responseJson.toString());
    }

    private Response handleAddFavourite(Request request) throws Exception {
        Thread.sleep(300);

        String requestBody = getRequestBody(request);
        JSONObject jsonBody = new JSONObject(requestBody);
        String productId = jsonBody.optString("productId");

        if (productId == null || productId.isEmpty()) {
            return createErrorResponse(request, "Product ID is required");
        }

        favouritedProducts.add(productId);

        JSONObject responseJson = new JSONObject();
        responseJson.put("success", true);
        responseJson.put("message", "Favourite added");

        return createSuccessResponse(request, responseJson.toString());
    }

    private Response handleGetFavourites(Request request) throws Exception {
        Thread.sleep(800);

        JSONArray favouritesArray = new JSONArray();

        // Create mock favourite objects
        for (String productId : favouritedProducts) {
            JSONObject favourite = new JSONObject();
            favourite.put("id", "fav_" + productId);
            favourite.put("productId", productId);
            favourite.put("user", "mock_user");
            favourite.put("createdAt", System.currentTimeMillis());

            // Add mock product data
            JSONObject product = new JSONObject();
            product.put("id", productId);
            product.put("name", "Mock Product " + productId);
            product.put("basePrice", 25000);
            product.put("image", "https://via.placeholder.com/150");
            favourite.put("product", product);

            favouritesArray.put(favourite);
        }

        Log.d(TAG, "Returning " + favouritedProducts.size() + " favourites");

        return createSuccessResponse(request, favouritesArray.toString());
    }

    private Response handleRemoveFavourite(Request request) throws Exception {
        Thread.sleep(300);

        String url = request.url().toString();
        String productId = url.substring(url.lastIndexOf("/") + 1);

        favouritedProducts.remove(productId);

        JSONObject responseJson = new JSONObject();
        responseJson.put("success", true);
        responseJson.put("message", "Favourite removed");

        return createSuccessResponse(request, responseJson.toString());
    }

    private String getRequestBody(Request request) throws IOException {
        if (request.body() != null) {
            okhttp3.RequestBody body = request.body();
            okio.Buffer buffer = new okio.Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        }
        return "{}";
    }

    private Response createSuccessResponse(Request request, String responseBody) {
        return new Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(responseBody, JSON))
                .addHeader("content-type", "application/json")
                .build();
    }

    private Response createErrorResponse(Request request, String errorMessage) {
        JSONObject errorJson = new JSONObject();
        try {
            errorJson.put("error", errorMessage);
            errorJson.put("success", false);
        } catch (Exception e) {
            Log.e(TAG, "Error creating error response", e);
        }

        return new Response.Builder()
                .code(400)
                .message("Bad Request")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(errorJson.toString(), JSON))
                .addHeader("content-type", "application/json")
                .build();
    }
}

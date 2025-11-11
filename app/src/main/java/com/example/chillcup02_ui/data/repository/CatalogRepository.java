package com.example.chillcup02_ui.data.repository;

import com.example.chillcup02_ui.data.api.ApiClient;
import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.data.dto.ProductFilterResponse;
import com.example.chillcup02_ui.data.mapper.CategoryMapper;
import com.example.chillcup02_ui.data.mapper.ProductMapper;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;

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
                    // Return mock categories if API fails
                    callback.onSuccess(getMockCategories());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                // Return mock categories if network fails
                callback.onSuccess(getMockCategories());
            }
        });
    }

    public void getAllProducts(ResultCallback<List<Product>> callback) {
        ApiClient.getApiService().getAllProducts().enqueue(new Callback<List<ProductDto>>() {
            @Override
            public void onResponse(Call<List<ProductDto>> call, Response<List<ProductDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = ProductMapper.toDomainList(response.body());
                    // Filter out banned products
                    products = products.stream()
                            .filter(product -> !product.isBanned())
                            .collect(java.util.stream.Collectors.toList());
                    callback.onSuccess(products);
                } else {
                    callback.onError("API returned unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductDto>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getProductsByCategory(String categoryId, ResultCallback<List<Product>> callback) {
        ApiClient.getApiService().getProductsByCategory(categoryId).enqueue(new Callback<ProductFilterResponse>() {
            @Override
            public void onResponse(Call<ProductFilterResponse> call, Response<ProductFilterResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getProducts() != null) {
                    List<Product> products = ProductMapper.toDomainList(response.body().getProducts());
                    callback.onSuccess(products);
                } else {
                    callback.onError("Failed to load products for category");
                }
            }

            @Override
            public void onFailure(Call<ProductFilterResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface ResultCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    // Mock data for testing when API is not available
    private List<Product> getMockProducts() {
        List<Product> mockProducts = new java.util.ArrayList<>();

        // Create mock categories
        Category coffeeCat = new Category("1", "Cà phê", "", "Các loại cà phê");
        Category teaCat = new Category("2", "Trà", "", "Các loại trà");
        Category juiceCat = new Category("3", "Nước ép", "", "Các loại nước ép");

        // Create mock products
        Product product1 = new Product("1", "Cà phê đen", "Cà phê đen đậm đà", 25000, "", "new", 4.5, null, null, "Store 1", java.util.Arrays.asList(coffeeCat), false);
        Product product2 = new Product("2", "Trà xanh", "Trà xanh tươi mát", 20000, "", "new", 4.2, null, null, "Store 1", java.util.Arrays.asList(teaCat), false);
        Product product3 = new Product("3", "Nước cam", "Nước cam ép tươi", 30000, "", "old", 4.0, null, null, "Store 2", java.util.Arrays.asList(juiceCat), false);
        Product product4 = new Product("4", "Cà phê sữa", "Cà phê sữa ngọt ngào", 28000, "", "new", 4.7, null, null, "Store 1", java.util.Arrays.asList(coffeeCat), false);
        Product product5 = new Product("5", "Trà ô long", "Trà ô long hảo hạng", 35000, "", "new", 4.3, null, null, "Store 2", java.util.Arrays.asList(teaCat), false);
        Product product6 = new Product("6", "Sinh tố bơ", "Sinh tố bơ tươi ngon", 40000, "", "old", 4.6, null, null, "Store 2", java.util.Arrays.asList(juiceCat), false);

        mockProducts.add(product1);
        mockProducts.add(product2);
        mockProducts.add(product3);
        mockProducts.add(product4);
        mockProducts.add(product5);
        mockProducts.add(product6);

        return mockProducts;
    }

    private List<Category> getMockCategories() {
        List<Category> mockCategories = new java.util.ArrayList<>();

        Category coffeeCat = new Category("1", "Cà phê", "", "Các loại cà phê");
        Category teaCat = new Category("2", "Trà", "", "Các loại trà");
        Category juiceCat = new Category("3", "Nước ép", "", "Các loại nước ép");

        mockCategories.add(coffeeCat);
        mockCategories.add(teaCat);
        mockCategories.add(juiceCat);

        return mockCategories;
    }
}

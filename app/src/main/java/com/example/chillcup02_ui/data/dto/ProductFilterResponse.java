package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductFilterResponse {
    @SerializedName("products")
    private List<ProductDto> products;

    public List<ProductDto> getProducts() { return products; }
    public void setProducts(List<ProductDto> products) { this.products = products; }
}

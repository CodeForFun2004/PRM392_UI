package com.example.chillcup02_ui.data.repository;

import android.content.Context;

import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.data.dto.SizeDto;
import com.example.chillcup02_ui.data.dto.ToppingDto;
import com.example.chillcup02_ui.data.mapper.ProductMapper;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {

    private final Context context;
    private final ProductMapper productMapper = new ProductMapper();
    private final Gson gson = new Gson();

    public ProductRepository(Context context) {
        this.context = context;
    }

    public List<Product> getProducts() {
        // First, load all possible sizes and toppings
        List<Size> allSizes = loadSizes();
        List<Topping> allToppings = loadToppings();

        // Now, load the products and map them
        Type productListType = new TypeToken<List<ProductDto>>() {}.getType();
        List<ProductDto> productDtos = readJsonAsset("mock_products.json", productListType);

        return productDtos.stream()
                .map(dto -> productMapper.toDomainModel(dto, allSizes, allToppings))
                .collect(Collectors.toList());
    }

    private List<Size> loadSizes() {
        Type sizeListType = new TypeToken<List<SizeDto>>() {}.getType();
        List<SizeDto> sizeDtos = readJsonAsset("mock_sizes.json", sizeListType);
        return sizeDtos.stream()
                .map(productMapper::toDomainModel)
                .collect(Collectors.toList());
    }

    private List<Topping> loadToppings() {
        Type toppingListType = new TypeToken<List<ToppingDto>>() {}.getType();
        List<ToppingDto> toppingDtos = readJsonAsset("mock_toppings.json", toppingListType);
        return toppingDtos.stream()
                .map(productMapper::toDomainModel)
                .collect(Collectors.toList());
    }

    private <T> List<T> readJsonAsset(String fileName, Type type) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream);
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

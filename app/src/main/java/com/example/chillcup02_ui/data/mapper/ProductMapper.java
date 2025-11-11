package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static Product toDomain(ProductDto dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBasePrice(dto.getBasePrice());
        product.setImage(dto.getImage());
        product.setStatus(dto.getStatus());
        product.setRating(dto.getRating());
        product.setBanned(dto.isBanned());

        // Map size options
        if (dto.getSizeOptions() != null) {
            List<Size> sizes = new ArrayList<>();
            for (ProductDto.SizeDto sizeDto : dto.getSizeOptions()) {
                Size size = new Size();
                size.setId(sizeDto.getId());
                size.setSize(sizeDto.getSize());
                size.setName(sizeDto.getName());
                size.setMultiplier(sizeDto.getMultiplier());
                size.setVolume(sizeDto.getVolume());
                sizes.add(size);
            }
            product.setSizeOptions(sizes);
        }

        // Map topping options
        if (dto.getToppingOptions() != null) {
            List<Topping> toppings = new ArrayList<>();
            for (ProductDto.ToppingDto toppingDto : dto.getToppingOptions()) {
                Topping topping = new Topping();
                topping.setId(toppingDto.getId());
                topping.setName(toppingDto.getName());
                topping.setPrice(toppingDto.getPrice());
                topping.setIcon(toppingDto.getIcon());
                toppings.add(topping);
            }
            product.setToppingOptions(toppings);
        }

        // Map store name (storeId is just a string in the API)
        if (dto.getStoreId() != null) {
            product.setStoreName(dto.getStoreId()); // For now, just use the ID as name
        }

        // Map categories
        if (dto.getCategoryId() != null) {
            List<Category> categories = new ArrayList<>();
            for (CategoryDto categoryDto : dto.getCategoryId()) {
                Category category = new Category();
                category.setId(categoryDto.getId());
                category.setName(categoryDto.getName());
                category.setIcon(categoryDto.getIcon());
                category.setDescription(categoryDto.getDescription());
                categories.add(category);
            }
            product.setCategories(categories);
        }

        return product;
    }

    public static List<Product> toDomainList(List<ProductDto> dtoList) {
        if (dtoList == null) return new ArrayList<>();

        android.util.Log.d("ProductMapper", "Mapping " + dtoList.size() + " products from DTO");

        List<Product> products = new ArrayList<>();
        for (ProductDto dto : dtoList) {
            Product product = toDomain(dto);
            if (product != null) {
                products.add(product);
            }
        }

        android.util.Log.d("ProductMapper", "Mapped to " + products.size() + " domain products");
        return products;
    }
}

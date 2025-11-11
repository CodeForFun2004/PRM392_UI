package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.domain.model.Product;

public class ProductMapper {

    public Product toDomainModel(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product.ProductStatus status = Product.ProductStatus.OLD; // Default value
        if (dto.getStatus() != null) {
            try {
                status = Product.ProductStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) { /* default to OLD */ }
        }

        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getBasePrice(),
                dto.getImage(),
                status,
                dto.getRating(),
                dto.getSizeOptions(),
                dto.getToppingOptions(),
                dto.getStoreId(),
                dto.getCategoryId(),
                dto.isBanned()
        );
    }
}

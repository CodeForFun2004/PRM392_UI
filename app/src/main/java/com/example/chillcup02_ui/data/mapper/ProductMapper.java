package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.ProductDto;
import com.example.chillcup02_ui.data.dto.SizeDto;
import com.example.chillcup02_ui.data.dto.ToppingDto;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public Size toDomainModel(SizeDto dto) {
        if (dto == null) return null;
        return new Size(dto.getId(), dto.getSize(), dto.getName(), dto.getMultiplier(), dto.getVolume());
    }

    public Topping toDomainModel(ToppingDto dto) {
        if (dto == null) return null;
        return new Topping(dto.getId(), dto.getName(), dto.getPrice(), dto.getIcon());
    }

    public Product toDomainModel(ProductDto dto, List<Size> allSizes, List<Topping> allToppings) {
        if (dto == null) return null;

        // Find the full Size objects corresponding to the IDs in the DTO
        List<Size> sizeOptions = allSizes.stream()
                .filter(size -> dto.getSizeOptions().contains(size.getId()))
                .collect(Collectors.toList());

        // Find the full Topping objects corresponding to the IDs in the DTO
        List<Topping> toppingOptions = allToppings.stream()
                .filter(topping -> dto.getToppingOptions().contains(topping.getId()))
                .collect(Collectors.toList());

        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getBasePrice(),
                dto.getImage(),
                dto.getStatus(),
                dto.getRating(),
                sizeOptions,      // Use the resolved list of Size objects
                toppingOptions,   // Use the resolved list of Topping objects
                dto.getStoreId(),
                dto.getCategoryId(),
                dto.isBanned()
        );
    }
}

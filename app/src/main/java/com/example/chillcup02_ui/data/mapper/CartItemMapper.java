package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.CartItemDto;
import com.example.chillcup02_ui.domain.model.CartItem;

public class CartItemMapper {

    public CartItem toDomainModel(CartItemDto dto) {
        if (dto == null) {
            return null;
        }

        return new CartItem(
                dto.getId(),
                dto.getUserId(),
                dto.getProductId(),
                dto.getSize(),
                dto.getToppings(), // Model holds a list of topping IDs
                dto.getQuantity(),
                dto.getPrice()
        );
    }
}

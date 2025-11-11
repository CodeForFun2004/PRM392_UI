package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.CartDto;
import com.example.chillcup02_ui.domain.model.Cart;

public class CartMapper {

    public Cart toDomainModel(CartDto dto) {
        if (dto == null) {
            return null;
        }

        return new Cart(
                dto.getId(),
                dto.getUserId(),
                dto.getCartItems(), // Model holds a list of cart item IDs
                dto.getSubtotal(),
                dto.getDeliveryFee(),
                dto.getDiscount(),
                dto.getTotal(),
                dto.getPromoCode(),
                dto.isCheckedOut()
        );
    }
}

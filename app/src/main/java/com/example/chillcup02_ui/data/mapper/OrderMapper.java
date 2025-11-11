package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.OrderDto;
import com.example.chillcup02_ui.data.dto.OrderItemDto;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.domain.model.OrderItem;
import com.example.chillcup02_ui.domain.model.Topping; // Correctly import the Topping model

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class OrderMapper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    public Order toDomainModel(OrderDto dto) {
        if (dto == null) return null;

        List<OrderItem> items = dto.getItems() != null ?
                dto.getItems().stream().map(this::toDomainModel).collect(Collectors.toList()) :
                Collections.emptyList();

        Date createdAtDate = null;
        if (dto.getCreatedAt() != null) {
            try {
                createdAtDate = dateFormat.parse(dto.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return new Order(
                dto.getId(),
                dto.getUserId(),
                dto.getStoreId(),
                dto.getOrderNumber(),
                items,
                dto.getSubtotal(),
                dto.getDeliveryFee(),
                dto.getTax(),
                dto.getTotal(),
                dto.getDeliveryAddress(),
                dto.getPhone(),
                toPaymentMethod(dto.getPaymentMethod()),
                dto.getQrCodeUrl(),
                dto.getDeliveryTime(),
                toOrderStatus(dto.getStatus()),
                dto.getCancelReason(),
                dto.getShipperAssigned(),
                createdAtDate
        );
    }

    public OrderItem toDomainModel(OrderItemDto dto) {
        if (dto == null) return null;

        // CORRECTED LOGIC:
        // Map from a list of ToppingDto to a list of the proper Topping domain model.
        List<Topping> toppings = dto.getToppings() != null ?
                dto.getToppings().stream()
                        // Your schema for toppings inside an order item may only have id and name.
                        // This is safe because we create a full Topping object, and the other fields (price, icon) will just be defaults.
                        .map(toppingDto -> new Topping(toppingDto.getId(), toppingDto.getName(), toppingDto.getPrice(), toppingDto.getIcon()))
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        return new OrderItem(
                dto.getId(),
                dto.getProductId(),
                dto.getName(),
                dto.getSize(),
                toppings, // This is now the correct List<Topping>
                dto.getQuantity(),
                dto.getPrice()
        );
    }

    private Order.OrderStatus toOrderStatus(String status) {
        if (status == null) return Order.OrderStatus.PENDING;
        try {
            return Order.OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Order.OrderStatus.PENDING;
        }
    }

    private Order.PaymentMethod toPaymentMethod(String method) {
        if (method == null) return Order.PaymentMethod.COD;
        try {
            return Order.PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Order.PaymentMethod.COD;
        }
    }
}

package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.ToppingDto;
import com.example.chillcup02_ui.domain.model.Topping;

public class ToppingMapper {

    public Topping toDomainModel(ToppingDto dto) {
        if (dto == null) {
            return null;
        }

        return new Topping(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getIcon()
        );
    }
}

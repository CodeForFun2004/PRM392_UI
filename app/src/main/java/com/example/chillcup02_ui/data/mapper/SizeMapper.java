package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.SizeDto;
import com.example.chillcup02_ui.domain.model.Size;

public class SizeMapper {

    public Size toDomainModel(SizeDto dto) {
        if (dto == null) {
            return null;
        }

        return new Size(
                dto.getId(),
                dto.getSize(), // sizeShortName
                dto.getName(),   // name
                dto.getMultiplier(), // priceMultiplier
                dto.getVolume()
        );
    }
}

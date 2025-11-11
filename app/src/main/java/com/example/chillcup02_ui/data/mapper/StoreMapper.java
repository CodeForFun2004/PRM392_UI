package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.StoreDto;
import com.example.chillcup02_ui.domain.model.Store;

public class StoreMapper {

    public Store toDomainModel(StoreDto dto) {
        if (dto == null) {
            return null;
        }

        return new Store(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getContact(),
                dto.getOpenHours(),
                dto.isActive(),
                dto.getMapUrl(),
                dto.getImage(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getStaff() // The staff field in DTO is the staffId
        );
    }
}

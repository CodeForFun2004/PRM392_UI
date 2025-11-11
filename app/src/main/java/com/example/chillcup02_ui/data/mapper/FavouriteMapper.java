package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.FavouriteDto;
import com.example.chillcup02_ui.domain.model.Favourite;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMapper {

    public static Favourite toDomain(FavouriteDto dto) {
        if (dto == null) return null;

        Favourite favourite = new Favourite();
        favourite.setId(dto.getId());
        favourite.setUserId(dto.getUserId());
        favourite.setProduct(ProductMapper.toDomain(dto.getProduct()));
        favourite.setCreatedAt(dto.getCreatedAt());

        return favourite;
    }

    public static FavouriteDto toDto(Favourite domain) {
        if (domain == null) return null;

        FavouriteDto dto = new FavouriteDto();
        dto.setId(domain.getId());
        dto.setUserId(domain.getUserId());
        dto.setProduct(ProductMapper.toDto(domain.getProduct()));
        dto.setCreatedAt(domain.getCreatedAt());

        return dto;
    }

    public static List<Favourite> toDomainList(List<FavouriteDto> dtoList) {
        if (dtoList == null) return new ArrayList<>();

        List<Favourite> domainList = new ArrayList<>();
        for (FavouriteDto dto : dtoList) {
            domainList.add(toDomain(dto));
        }
        return domainList;
    }

    public static List<FavouriteDto> toDtoList(List<Favourite> domainList) {
        if (domainList == null) return new ArrayList<>();

        List<FavouriteDto> dtoList = new ArrayList<>();
        for (Favourite domain : domainList) {
            dtoList.add(toDto(domain));
        }
        return dtoList;
    }
}

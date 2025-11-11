package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.domain.model.Category;

public class CategoryMapper {

    public Category toDomainModel(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return new Category(
                dto.getId(),
                dto.getCategory(), // The "category" field in the DTO maps to the "name" in the model
                dto.getIcon(),
                dto.getDescription()
        );
    }
}

package com.example.chillcup02_ui.data.mapper;

import com.example.chillcup02_ui.data.dto.CategoryDto;
import com.example.chillcup02_ui.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category toDomain(CategoryDto dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setIcon(dto.getIcon());
        category.setDescription(dto.getDescription());

        return category;
    }

    public static List<Category> toDomainList(List<CategoryDto> dtoList) {
        if (dtoList == null) return new ArrayList<>();

        List<Category> categories = new ArrayList<>();
        for (CategoryDto dto : dtoList) {
            categories.add(toDomain(dto));
        }

        return categories;
    }
}

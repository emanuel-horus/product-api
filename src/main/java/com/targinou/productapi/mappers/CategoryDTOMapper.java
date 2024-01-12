package com.targinou.productapi.mappers;

import com.targinou.productapi.dto.CategoryDTO;
import com.targinou.productapi.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOMapper implements DtoMapper<Category, CategoryDTO> {

    @Override
    public CategoryDTO toDto(Category entity) {
        return new CategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.isActive(),
                entity.getType()
        );
    }

    @Override
    public Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.id());
        category.setName(dto.name());
        category.setActive(dto.active());
        category.setType(dto.type());
        return category;
    }
}

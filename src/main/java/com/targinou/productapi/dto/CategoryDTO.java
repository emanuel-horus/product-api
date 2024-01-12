package com.targinou.productapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targinou.productapi.model.enums.Type;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDTO(
        long id,
        String name,
        boolean active,
        Type type
) implements EntityDTO {

    @Override
    public CategoryDTO toResponse() {
        return new CategoryDTO(this.id(), this.name(), this.active(), this.type());
    }
}

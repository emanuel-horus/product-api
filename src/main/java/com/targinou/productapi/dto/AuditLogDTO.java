package com.targinou.productapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.targinou.productapi.model.enums.ActionType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuditLogDTO(
        Long id,
        ProductDTO product,
        UserDTO user,
        ActionType actionType,
        String description
) implements EntityDTO {

    @Override
    public AuditLogDTO toResponse() {
        return new AuditLogDTO(this.id(), this.product(), this.user(), this.actionType(), this.description());
    }
}
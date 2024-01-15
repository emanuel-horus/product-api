package com.targinou.productapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDTO(
        long id,
        String name,
        Boolean active,
        String sku,
        CategoryDTO category,
        Double costPrice,
        Double icms,
        Double salePrice,
        String productImage,
        Integer stockQuantity
) implements EntityDTO {

    @Override
    public ProductDTO toResponse() {
        return new ProductDTO(
                this.id(),
                this.name(),
                this.active(),
                this.sku(),
                this.category().toResponse(),
                this.costPrice(),
                this.icms(),
                this.salePrice(),
                this.productImage(),
                this.stockQuantity()
        );
    }
}

package com.targinou.productapi.dto;

public record ProductSummaryDTO(
        Long id,
        String name,
        Double costPrice,
        Double totalCost,
        Integer stockQuantity,
        Double salePrice,
        Double expectedTotalValue
) {
}

package com.targinou.productapi.mappers;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.CategoryDTO;
import com.targinou.productapi.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOMapper implements DtoMapper<Product, ProductDTO> {

    private final CategoryDTOMapper categoryDTOMapper;

    public ProductDTOMapper(CategoryDTOMapper categoryDTOMapper) {
        this.categoryDTOMapper = categoryDTOMapper;
    }

    @Override
    public ProductDTO toDto(Product entity) {
        CategoryDTO categoryDTO = entity.getCategory() != null ? categoryDTOMapper.toDto(entity.getCategory()) : null;

        return new ProductDTO(
                entity.getId(),
                entity.getName(),
                entity.isActive(),
                entity.getSku(),
                categoryDTO,
                entity.getCostPrice(),
                entity.getIcms(),
                entity.getSalePrice(),
                entity.getProductImage(),
                entity.getStockQuantity()
        );
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.id());
        product.setName(dto.name());
        product.setActive(dto.active());
        product.setSku(dto.sku());
        product.setCategory(dto.category() != null ? categoryDTOMapper.toEntity(dto.category()) : null);
        product.setCostPrice(dto.costPrice());
        product.setIcms(dto.icms());
        product.setSalePrice(dto.salePrice());
        product.setProductImage(dto.productImage());
        product.setStockQuantity(dto.stockQuantity());

        return product;
    }
}

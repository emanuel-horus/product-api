package com.targinou.productapi.mappers;

import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.model.enums.Role;
import com.targinou.productapi.service.AuthenticationService;
import com.targinou.productapi.service.FieldVisibilityService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductDTOMapper implements DtoMapper<Product, ProductDTO> {

    private final CategoryDTOMapper categoryDTOMapper;
    private final FieldVisibilityService fieldVisibilityService;
    private final AuthenticationService authenticationService;

    public ProductDTOMapper(CategoryDTOMapper categoryDTOMapper,
                            FieldVisibilityService fieldVisibilityService,
                            AuthenticationService authenticationService) {
        this.categoryDTOMapper = categoryDTOMapper;
        this.fieldVisibilityService = fieldVisibilityService;
        this.authenticationService = authenticationService;
    }

    @Override
    public ProductDTO toDto(Product entity) {
        var user = authenticationService.getAuthenticatedUser();
        Set<String> visibleFields = fieldVisibilityService.getVisibleFieldsForRole(user.getRole());

        var id = entity.getId();
        var name = entity.getName();
        var active = entity.isActive();

        var sku = visibleFields.contains("sku") || user.getRole().equals(Role.ADMIN) ? entity.getSku() : null;
        var icms = visibleFields.contains("icms") || user.getRole().equals(Role.ADMIN) ? entity.getIcms() : null;
        var costPrice = visibleFields.contains("costPrice") || user.getRole().equals(Role.ADMIN) ? entity.getCostPrice() : null;
        var salePrice = visibleFields.contains("salePrice") || user.getRole().equals(Role.ADMIN) ? entity.getSalePrice() : null;
        var productImage = visibleFields.contains("productImage") || user.getRole().equals(Role.ADMIN) ? entity.getProductImage() : null;
        var stockQuantity = visibleFields.contains("stockQuantity") || user.getRole().equals(Role.ADMIN) ? entity.getStockQuantity() : null;
        var category = visibleFields.contains("category") || user.getRole().equals(Role.ADMIN) ? entity.getCategory() != null ? categoryDTOMapper.toDto(entity.getCategory()) : null : null;

        return new ProductDTO(id, name, active, sku, category, icms, costPrice, salePrice, productImage, stockQuantity);
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

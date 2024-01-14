package com.targinou.productapi.controller;

import com.targinou.productapi.dto.ApiResponseDTO;
import com.targinou.productapi.dto.ProductDTO;
import com.targinou.productapi.dto.ProductSearchFilterDTO;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.service.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@Validated
public class ProductController extends GenericController<Product, ProductDTO, ProductService> {

    protected ProductController(ProductService service) {
        super(service);
    }

    /**
     * Retrieves a pageable list of products with detailed information based on the provided filter criteria.
     *
     * @param pageable The Pageable object specifying the page number, size, and sorting criteria.
     * @param name     (Optional) The name filter to narrow down the search by product name.
     * @return A ResponseEntity containing ApiResponseDTO with a Page of ProductDTO.
     */
    @GetMapping("/find-all")
    public ResponseEntity<ApiResponseDTO<Page<ProductDTO>>> findAll(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Double costPrice,
            @RequestParam(required = false) Double icms,
            @RequestParam(required = false) Double salePrice,
            @RequestParam(required = false) Integer stockQuantity,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection) {


        ProductSearchFilterDTO searchDTO = new ProductSearchFilterDTO(
                pageable, id, name, sku, categoryId, userId, costPrice, icms, salePrice, stockQuantity, orderBy, orderDirection
        );

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        true,
                        "Success: Products retrieved successfully.",
                        service.searchByFilter(searchDTO),
                        null));
    }

}
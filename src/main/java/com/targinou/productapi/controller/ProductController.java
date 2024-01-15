package com.targinou.productapi.controller;

import com.targinou.productapi.dto.*;
import com.targinou.productapi.model.Product;
import com.targinou.productapi.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/products")
@Validated
@SecurityRequirement(name = "bearerAuth")
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

    @GetMapping("/find-summary")
    public ResponseEntity<ApiResponseDTO<Page<ProductSummaryDTO>>> findSummary(
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
                        "Success: Product summaries retrieved successfully.",
                        service.searchSummaryByFilter(searchDTO),
                        null));
    }

    @PostMapping("/generate-report")
    public ResponseEntity<Resource> generateProductReport(
            @ParameterObject Pageable pageable,
            @RequestBody ProductReportRequestDTO reportRequestDTO,
            @RequestParam(defaultValue = "CSV") String format) throws IOException {

        reportRequestDTO.getSearchFilterDTO().setPageable(pageable);
        String filePath = service.searchByReportFilter(reportRequestDTO, format);
        Resource fileResource = new UrlResource("file:" + filePath);

        String mediaType = "CSV".equalsIgnoreCase(format) ? "text/csv" : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        String fileName = "CSV".equalsIgnoreCase(format) ? "report.csv" : "report.xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileResource);
    }


}